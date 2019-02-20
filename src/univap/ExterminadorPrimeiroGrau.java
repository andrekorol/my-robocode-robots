package univap;

import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import java.awt.*;

public class ExterminadorPrimeiroGrau extends Robot {
	static int count = 0;
	double gunTurnAmt; 
	String trackName;

	public void run() {
		// A morte não é o fim, é apenas um caminho que todos iremos tomar.
		setBodyColor(Color.yellow);
		setGunColor(Color.green);
		setRadarColor(Color.pink);
		setScanColor(Color.pink);
		setBulletColor(Color.pink);

		trackName = null;
		setAdjustGunForRobotTurn(true);
		gunTurnAmt = 10;
		
		while (true) {
			turnGunRight(gunTurnAmt);
			count++;
			if (count > 2) {
				gunTurnAmt = -10;
			}
			if (count > 5) {
				gunTurnAmt = 10;
			}
			if (count > 11) {
				trackName = null;
			}
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		if (trackName != null && !e.getName().equals(trackName)) {
			return;
		}
		if (trackName == null) {
			trackName = e.getName();
			out.println("Tracking " + trackName);
		}
		count = 0;
		if (e.getDistance() > 150) {
			gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
			turnGunRight(gunTurnAmt);
			turnRight(e.getBearing());
			ahead(e.getDistance() - 140);
			return;
		}

		gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		turnGunRight(gunTurnAmt);
		fire(3);

		if (e.getDistance() < 100) {
			if (e.getBearing() > -90 && e.getBearing() <= 90) {
				back(40);
			} else {
				ahead(40);
			}
		}
		scan();
	}

	public void onHitRobot(HitRobotEvent e) {
		if (trackName != null && !trackName.equals(e.getName())) {
			out.println("Tracking " + e.getName() + " due to collision");
		}
		trackName = e.getName();
		gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		turnGunRight(gunTurnAmt);
		fire(3);
		back(50);
	}

	public void onWin(WinEvent e) {
		for (int i = 0; i < 50; i++) {
			turnRight(30);
			turnLeft(30);
		}
	}
}
