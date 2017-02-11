package com.grsu.reader.serial;

import com.grsu.reader.beans.LessonBean;
import com.grsu.reader.entities.Student;
import com.grsu.reader.utils.EntityUtils;
import com.grsu.reader.utils.SerialUtils;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import static com.grsu.reader.constants.Constants.*;

public class SerialListener implements SerialPortEventListener {
	private SerialPort serialPort;
	private LessonBean lessonBean;

	public SerialListener(SerialPort serialPort, LessonBean lessonBean) {
		this.serialPort = serialPort;
		this.lessonBean = lessonBean;
	}

	public void serialEvent(SerialPortEvent event) {
		if (event.isRXCHAR() && event.getEventValue() > 0) {
			System.out.println("---");
			try {
				//Получаем ответ от устройства, обрабатываем данные и т.д.
				String data = serialPort.readString(event.getEventValue());
				System.out.println("Data received: ' " + data + " '");
				//И снова отправляем запрос
				if (data.startsWith(SERIAL_CARD_UID_PREFIX)) {
					String uid = data.replace(SERIAL_CARD_UID_PREFIX, "").substring(0, 8);
					System.out.println("Received card with uid: " + uid);

					Student student = EntityUtils.getPersonByUid(lessonBean.getAbsentStudents(), uid);
					if (student == null) {
						if (EntityUtils.getPersonByUid(lessonBean.getPresentStudents(), uid) != null) {
							System.out.println("Student not added. Reason: Uid[ " + uid + " ] already exists.");
						} else {
							student = EntityUtils.getPersonByUid(lessonBean.getAllStudents(), uid);
						}
					}
					if (student != null) {
						SerialUtils.sendResponse(
								serialPort,
								lessonBean.processStudent(student),
								lessonBean.isSoundEnabled()
						);
					} else {
						System.out.println("Student not added. Reason: Uid[ " + uid + " ] is not from present groups.");
						SerialUtils.sendResponse(
								serialPort,
								false,
								lessonBean.isSoundEnabled()
						);
					}
				}
			} catch (SerialPortException ex) {
				System.out.println(ex);
			}
		}
	}
}
