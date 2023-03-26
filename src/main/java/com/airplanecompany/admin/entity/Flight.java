package com.airplanecompany.admin.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id", nullable = false)
    private Long flightId;
    @Basic
    @Column(name = "airline_name", nullable = false, length = 45)
    private String airlineName;
    @Basic
    @Column(name = "from_location", nullable = false, length = 45)
    private String fromLocation;
    @Basic
    @Column(name = "to_location", nullable = false, length = 45)
    private String toLocation;
    @Basic
    @Column(name = "departure_time", nullable = false, length = 10)
    private String departureTime;
    @Basic
    @Column(name = "arrival_time", nullable = false, length = 10)
    private String arrivalTime;
    @Basic
    @Column(name = "duration", nullable = false, length = 10)
    private String duration;
    @Basic
    @Column(name = "total_seats", nullable = false)
    private int totalSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pilot_id", referencedColumnName = "pilot_id", nullable = false)
    private Pilot pilot;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "board_in",
    joinColumns = {@JoinColumn(name="flight_id")},
    inverseJoinColumns = {@JoinColumn(name="passenger_id")})
    private Set<Passenger> passengers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return totalSeats == flight.totalSeats && flightId.equals(flight.flightId) && Objects.equals(airlineName, flight.airlineName) && Objects.equals(fromLocation, flight.fromLocation) && Objects.equals(toLocation, flight.toLocation) && Objects.equals(departureTime, flight.departureTime) && Objects.equals(arrivalTime, flight.arrivalTime) && Objects.equals(duration, flight.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId, airlineName, fromLocation, toLocation, departureTime, arrivalTime, duration, totalSeats);
    }

    public void assignPassengerToFlight(Passenger passenger) {
        this.passengers.add(passenger);
        passenger.getFlights().add(this);
    }

    public void removePassengerFromFlight(Passenger passenger) {
        this.passengers.remove(passenger);
        passenger.getFlights().remove(this);
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Flight() {
    }

    public Flight(String airlineName, String fromLocation, String toLocation, String departureTime, String arrivalTime, String duration, int totalSeats, Pilot pilot) {
        this.airlineName = airlineName;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.totalSeats = totalSeats;
        this.pilot = pilot;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "airlineName='" + airlineName + '\'' +
                ", fromLocation='" + fromLocation + '\'' +
                ", toLocation='" + toLocation + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", duration='" + duration + '\'' +
                ", totalSeats=" + totalSeats +
                '}';
    }
}
