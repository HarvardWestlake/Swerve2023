// package frc.robot.Auto.Drive;

// import frc.robot.Auto.Position;
// import frc.robot.Auto.PositioningSystem;
// import frc.robot.Drive.PositionedDrive;
// import frc.robot.Util.AngleMath;
// import frc.robot.Util.DeSpam;
// import frc.robot.Util.PDConstant;
// import frc.robot.Util.PDController;
// import frc.robot.Util.Promise;
// import frc.robot.Util.Tickable;
// import frc.robot.Util.Vector2;

// public class AutonomousDrive implements Tickable {
//     PositionedDrive drive;
//     PDController xController;
//     PDController yController;
//     PDController turnController;
//     PositioningSystem pos;

//     double targetX;
//     double targetY;
//     double targetAngle; // turn angle

//     DriveCommand command = null;

//     public Promise exec(DriveCommand command) {
//         return exec(command, false);
//     }

//     public Promise exec(DriveCommand command, boolean override) {
//         var promise = new Promise();
//         if (!override && this.command != null) {
//             throw new IllegalStateException("Cannot execute command while another command is running");
//         }
//         this.command = command;
//         command.init(drive, () -> {
//             if (this.command == command)
//                 this.command = null;
//             promise.resolve();
//         });
//         return promise;
//     }

//     public AutonomousDrive(PositionedDrive drive, PositioningSystem pos, PDConstant goConstants,
//             PDConstant turnConstants) {
//         this.drive = drive;
//         this.pos = pos;
//         this.xController = new PDController(goConstants);
//         this.yController = new PDController(goConstants);
//         this.turnController = new PDController(turnConstants);
//         this.targetX = pos.getPosition().x;
//         this.targetY = pos.getPosition().y;
//         this.targetAngle = pos.getAngle();
//     }

//     public void reset() {
//         targetX = pos.getPosition().x;
//         targetY = pos.getPosition().y;
//         targetAngle = AngleMath.toTurnAngle(pos.getAngle());
//         xController.reset();
//         yController.reset();
//         turnController.reset();
//     }

//     DeSpam deSpam = new DeSpam(0.1);

//     public void tick(double dTime) {
//         if (command != null) {
//             Position tick = command.tick(dTime);
//             targetX += tick.dX;
//             targetY += tick.dY;
//             targetAngle += tick.dAngle;
//         }
//         double xCorrect = xController.solve(targetX - pos.getPosition().x);
//         double yCorrect = yController.solve(targetY - pos.getPosition().y);

//         double turnCorrect = turnController
//                 .solve(AngleMath.getDelta(AngleMath.toTurnAngle(pos.getAngle()), targetAngle));

//         Vector2 goCorrect = new Vector2(xCorrect, yCorrect).rotate(-AngleMath.toTurnAngle(pos.getAngle()));

//         deSpam.exec(() -> {
//             // print targetx, currentx, targety, currenty, targetangle, currentangle
//             System.out.println("targetX: " + targetX + ", currentX: " + pos.getPosition().x);
//             // + ", targetY: " + targetY
//             // + ", currentY: " + pos.getPosition().y + ", \ntargetAngle: " + targetAngle
//             // + ", currentAngle: "
//             // + AngleMath.toTurnAngle(pos.getAngle()));
//             System.out.println(goCorrect.getTurnAngleDeg());
//         });
//         if (2 < turnController.getLastError() / 3 + xController.getLastError() + yController.getLastError())
//             drive.power(goCorrect.getMagnitude(), goCorrect.getAngleDeg(), turnCorrect, false);
//         else
//             drive.stopGoPower();
//     }
// }