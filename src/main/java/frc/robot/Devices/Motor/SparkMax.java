package frc.robot.Devices.Motor;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Devices.AnyMotor;

public class SparkMax extends AnyMotor {
    private CANSparkMax maxspark;
    private RelativeEncoder encoder;

    public void setCurrentLimit(int amps) {
        maxspark.setSmartCurrentLimit(amps);
    }

    final int id;
    public int getID() {
        return id;
    }

    public SparkMax(int canID, boolean isReversed) {
        super(isReversed);

        id = canID;

        this.maxspark = new CANSparkMax(canID, MotorType.kBrushless);
        this.maxspark.restoreFactoryDefaults();
        this.encoder = maxspark.getEncoder();
        maxspark.setIdleMode(IdleMode.kCoast);

        // sends a max of ten amps when stalling, 100 amps when not
        maxspark.setSmartCurrentLimit(40, 100);
    }

    public void setBrake(boolean brake) {
        maxspark.setIdleMode((brake) ? IdleMode.kBrake : IdleMode.kCoast);
    }

    public SparkMax(int canID, boolean isReversed, boolean brakeMode) {
        this(canID, isReversed);
        if (brakeMode)
            maxspark.setIdleMode(IdleMode.kBrake);
        else
            maxspark.setIdleMode(IdleMode.kCoast);
    }

    protected void uSetVoltage(double volts) {
        maxspark.setVoltage(volts);
    }

    protected double uGetRevs() {
        return encoder.getPosition();
    }

    public void setCurrentLimit (int stall, int free)
    {
        maxspark.setSmartCurrentLimit(stall, free);
    }

    public void stop() {
        maxspark.stopMotor();
    }
}
