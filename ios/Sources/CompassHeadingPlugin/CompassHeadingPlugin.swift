import Foundation
import Capacitor
import CoreLocation

@objc(CompassHeadingPlugin)
public class CompassHeadingPlugin: CAPPlugin, CAPBridgedPlugin, CLLocationManagerDelegate {
    var locationManager: CLLocationManager!

    public let identifier = "CompassHeadingPlugin"
    public let jsName = "CompassHeading"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "start", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "stop", returnType: CAPPluginReturnPromise)
    ]

    override public func load() {
        self.locationManager = CLLocationManager()
        self.locationManager.delegate = self
    }

    @objc func start(_ call: CAPPluginCall) {
        self.locationManager.requestWhenInUseAuthorization()
        
        if CLLocationManager.headingAvailable() {
            self.locationManager.startUpdatingLocation()
            self.locationManager.startUpdatingHeading()
        }
        call.resolve()
    }

    @objc func stop(_ call: CAPPluginCall) {
        locationManager?.stopUpdatingHeading()
        locationManager?.stopUpdatingLocation()
        call.resolve()
    }

    public func locationManager(_ manager: CLLocationManager, didUpdateHeading newHeading: CLHeading) {
        let heading = newHeading.trueHeading > 0 ? newHeading.trueHeading : -1 * newHeading.magneticHeading

        let result: [String: Any] = [
            "heading": heading,
            "accuracy": newHeading.headingAccuracy
        ]
        notifyListeners("headingChange", data: result)
    }

    public func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("❌ Compass error: \(error.localizedDescription)")
    }

    override public func removeAllListeners(_ call: CAPPluginCall) {
        locationManager?.stopUpdatingHeading()
        locationManager?.stopUpdatingLocation()
        call.resolve()
    }
}
