package com.grsu.reader.utils;

import com.grsu.reader.beans.LessonBean;
import com.grsu.reader.serial.SerialListener;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.util.regex.Pattern;

import static com.grsu.reader.constants.Constants.*;

public class SerialUtils {
	private static SerialPort serialPort;
	private static Thread shutdownHook;
	private static final String EXCEPTION_PORT_NOT_FOUND = "Port not found";
	private static final String EXCEPTION_PORT_BUSY = "Port busy";

	public static void sendResponse(SerialPort serialPort, boolean success, boolean soundEnabled) throws SerialPortException {
		if (success && soundEnabled) {
			serialPort.writeString(SERIAL_STATUS_OK);
		}
		if (success && !soundEnabled) {
			serialPort.writeString(SERIAL_STATUS_OL);
		}
		if (!success && soundEnabled) {
			serialPort.writeString(SERIAL_STATUS_ER);
		}
		if (!success && !soundEnabled) {
			serialPort.writeString(SERIAL_STATUS_EL);
		}
	}

	public static boolean disconnect() {
		if (serialPort != null && serialPort.isOpened()) {
			try {
				serialPort.closePort();
//				Runtime.getRuntime().removeShutdownHook(shutdownHook);
				System.out.println("Reader disconnected");
			} catch (SerialPortException e) {
				e.printStackTrace();
				return false;
			} catch (IllegalStateException | SecurityException e) {
				return false;
			}
		}
		return true;
	}

	public static boolean connect(LessonBean lessonBean) {
		boolean connected = false;
		String lastConnectionPort = PropertyUtils.getProperty("last.connection.port");
		if (lastConnectionPort != null && !lastConnectionPort.isEmpty()) {
			System.out.println("Found previously saved port [ " + lastConnectionPort + " ]. Trying to connect...");
			connected = connect(lastConnectionPort, lessonBean);
		}
		return connected || connect(findReader(), lessonBean);
	}

	private static boolean connect(String port, LessonBean lessonBean) {
		return connect(port, lessonBean, true);
	}

	private static boolean connect(String port, LessonBean lessonBean, boolean retryIfPortBusy) {
		if (port == null) return false;

		serialPort = new SerialPort(port);
		try {
			//Открываем порт
			serialPort.openPort();
			//Выставляем параметры
			serialPort.setParams(
					SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE,
					false, false);
			//Включаем аппаратное управление потоком
			//serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
			//                              SerialPort.FLOWCONTROL_RTSCTS_OUT);
			//upd: Отключаем так как на windows не работает порт нормально
			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

			//Устанавливаем ивент лисенер и маску
			Thread.sleep(1000);
			serialPort.addEventListener(
					new SerialListener(serialPort, lessonBean),
					SerialPort.MASK_RXCHAR
			);
			System.out.println("Serial port listener added. Port: " + port);

			//Отправляем запрос устройству
			Thread.sleep(1000);
			serialPort.writeString(SERIAL_SET_START);

			shutdownHook = getShutdownHook(serialPort);
			Runtime.getRuntime().addShutdownHook(shutdownHook);
		} catch (SerialPortException e) {
			if (EXCEPTION_PORT_NOT_FOUND.equals(e.getExceptionType())) {
				System.out.println("Connection failed. Previously saved port not found.");
			} else if (EXCEPTION_PORT_BUSY.equals(e.getExceptionType())) {
				if (retryIfPortBusy) {
					System.out.println("Port [ " + serialPort.getPortName() + " ] is busy. Trying to reconnect...");
					disconnect();
					connect(port, lessonBean, false);
				} else {
					System.out.println("Connection failed. Port [ " + serialPort.getPortName() + " ] is busy.");
				}
			} else {
				e.printStackTrace();
			}
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		PropertyUtils.setProperty("last.connection.port", port);
		return true;
	}

	private static String findReader() {
		System.out.println("Reader search started.");
		String foundAt = null;
		for (String portName : getPortNames()) {
			final SerialPort serialPort = new SerialPort(portName);
			try {
				serialPort.openPort();
				serialPort.setParams(
						SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE,
						false, false);
				serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

				Thread.sleep(1500);
				serialPort.writeString(SERIAL_GET_INFO);

				Thread.sleep(500);
				String info = serialPort.readString();

				serialPort.closePort();

				if (info != null && info.startsWith(SERIAL_READER_INFO_PREFIX)) {
					foundAt = portName;
					break;
				} else {
					System.out.println("Port [ " + portName + " ] unsuccessful. Reason: No response");
				}
			} catch (SerialPortException ex) {
				System.out.println("Port [ " + portName + " ] unsuccessful. Reason: " + ex.getExceptionType());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (foundAt != null) {
			System.out.println("Reader found at port: " + foundAt);
		} else {
			System.out.println("Reader not found.");
		}
		return foundAt;
	}

	private static String[] getPortNames() {
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			return SerialPortList.getPortNames("/dev/", Pattern.compile("cu."));
		}
		return SerialPortList.getPortNames();
	}

	private static Thread getShutdownHook(final SerialPort serialPort) {
		return new Thread() {
			@Override
			public void run() {
				if (serialPort != null) {
					try {
						serialPort.closePort();
						System.out.println("Serial port closed by shutdown hook!");
					} catch (SerialPortException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
}
