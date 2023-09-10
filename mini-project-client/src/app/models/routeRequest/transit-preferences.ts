import { AllowedTravelModes } from "./allowed-travel-modes"
import { TransitRoutingPreference } from "./transit-routing-preference"

export interface TransitPreferences {
  allowedTravelModes: AllowedTravelModes[]
  routingPreference: TransitRoutingPreference
}
