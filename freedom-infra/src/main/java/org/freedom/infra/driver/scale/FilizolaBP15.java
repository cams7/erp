package org.freedom.infra.driver.scale;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

import org.freedom.infra.functions.StringFunctions;

public class FilizolaBP15 extends AbstractScale {

	public static final byte ENQ = 5;

	public static final byte STX = 2;

	public static final byte ETX = 3;

	public static final String INSTABLE = "IIIII";// "|||||";

	public static final String NEGATIVE = "NNNNN";

	public static final String OVERFLOW = "SSSSS";

	public static final String NOME_BAL = "Filizola BP-15";

	private byte[] command = { ENQ };

	public void setCom(Integer com) {
		this.com = com;
	}

	public void initialize(Integer com, Integer timeout, Integer baudrate, Integer databits, Integer stopbits, Integer parity) {

		this.com = com;

		FilizolaBP15.TIMEOUT_READ = 2000;

		TIMEOUT_ACK = 1500;

		serialParams.setTimeout(timeout);
		serialParams.setBauderate(baudrate);
		serialParams.setDatabits(databits);
		serialParams.setStopbits(stopbits);
		serialParams.setParity(parity);

		activePort(null);

		sendCmd(command, 7);

		readReturn();

	}

	public String getName() {
		return NOME_BAL;
	}

	public FilizolaBP15() {

		super();

	}

	protected void readReturn() {

		try {

			if (buffer != null) {

				String reading = new String(buffer);
				System.out.println(reading);

			}
			else {
				System.out.println("Buffer is null!");
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Date getDate() {

		return new Date();
	}

	public Time getTime() {

		Time tm = null;
		tm = new Time(getDate().getTime());

		return tm;

	}

	public BigDecimal getWeight() {

		BigDecimal weight = null;

		if (buffer != null && buffer.length > 0) {

			if (buffer[0] == STX && buffer[6] == ETX) {

				String strweight = new String(buffer);

				strweight = strweight.substring(1, strweight.length() - 1);

				strweight = StringFunctions.alltrim(strweight);

				System.out.print(strweight);

				if (FilizolaBP15.INSTABLE.equals(strweight)) {
					System.out.println(AbstractScale.MESSAGE_UNSTABLE);
					setLog(AbstractScale.LOG_MESSAGE, AbstractScale.MESSAGE_UNSTABLE);
					return weight;
				}
				else if (FilizolaBP15.NEGATIVE.equals(strweight)) {
					System.out.println(AbstractScale.MESSAGE_NEGATIVE_VALUE);
					setLog(AbstractScale.LOG_MESSAGE, AbstractScale.MESSAGE_NEGATIVE_VALUE);

					return weight;
				}
				else if (FilizolaBP15.OVERFLOW.equals(strweight)) {
					System.out.println("Escale return a weight overflow, try again!");
					return weight;
				}

				weight = new BigDecimal(String.valueOf(strweight));

			}
			else {
				System.out.println("Invalid return!");
			}

		}

		return weight;
	}

	public void run() {
	};

	public ScaleResult parseString() {
		ScaleResult result = new ScaleResult();
		result.setDate(getDate());
		result.setTime(getTime());
		result.setWeight(getWeight());
		return result;
	};

}
