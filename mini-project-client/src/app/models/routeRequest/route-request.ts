import { RouteTravelMode } from "./route-travel-mode"
import { RoutingPreference } from "./routing-preference"
import { TransitPreferences } from "./transit-preferences"

export interface RouteRequest {
  origin: {placeId: string}
  destination: {placeId: string}
  travelMode: RouteTravelMode
  routingPreference?: RoutingPreference
  departureTime?: string
  arrivalTime?: string
  computeAlternativeRoutes: boolean
  units?: string
}
