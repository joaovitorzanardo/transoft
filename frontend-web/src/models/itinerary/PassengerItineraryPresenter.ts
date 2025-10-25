import type PassengerPresenter from "../PassengerPresenter";

export default interface PassengerItineraryPresenter {
    passenger: PassengerPresenter;
    status: string;
}