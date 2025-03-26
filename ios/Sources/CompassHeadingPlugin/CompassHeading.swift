import Foundation

@objc public class CompassHeading: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
