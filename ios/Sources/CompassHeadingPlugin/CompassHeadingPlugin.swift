import Foundation
import Capacitor
import CoreMotion

@objc(CompassHeadingPlugin)
public class CompassHeadingPlugin: CAPPlugin {

    let motionManager = CMMotionManager()

    @objc func start(_ call: CAPPluginCall) {
        if motionManager.isDeviceMotionAvailable {
            motionManager.deviceMotionUpdateInterval = 0.2
            motionManager.startDeviceMotionUpdates(using: .xArbitraryZVertical, to: .main) { (motion, error) in
                if let motion = motion {
                    let headingRadians = motion.attitude.yaw
                    let headingDegrees = (headingRadians * 180 / .pi + 360).truncatingRemainder(dividingBy: 360)

                    let result: [String: Any] = [
                        "heading": headingDegrees,
                        "accuracy": motion.magneticField.accuracy.rawValue
                    ]
                    self.notifyListeners("headingChange", data: result)
                }
            }
            call.resolve()
        } else {
            call.reject("DeviceMotion not available")
        }
    }

    @objc func stop(_ call: CAPPluginCall) {
        motionManager.stopDeviceMotionUpdates()
        call.resolve()
    }

    override public func removeAllListeners(_ call: CAPPluginCall) {
        motionManager.stopDeviceMotionUpdates()
        call.resolve()
    }
}
