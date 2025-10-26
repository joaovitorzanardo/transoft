import PassengerPresenter from "./PassengerPresenter";

export default interface PassengerItineraryPresenter {
    passenger: PassengerPresenter;
    status: 'CONFIRMADO' | 'NAO_VAI';
}