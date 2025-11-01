package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.vehicle.VehicleDto;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenter;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclePresenterList;
import br.com.transoft.backend.dto.vehicle.presenter.VehiclesStatsPresenter;
import br.com.transoft.backend.entity.Company;
import br.com.transoft.backend.entity.Itinerary;
import br.com.transoft.backend.entity.Vehicle;
import br.com.transoft.backend.entity.VehicleModel;
import br.com.transoft.backend.entity.route.Route;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.ItineraryRepository;
import br.com.transoft.backend.repository.RouteRepository;
import br.com.transoft.backend.repository.VehicleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleModelService vehicleModelService;
    private final RouteRepository routeRepository;
    private final ItineraryRepository itineraryRepository;

    public VehicleService(VehicleRepository vehicleRepository, VehicleModelService vehicleModelService, RouteRepository routeRepository, ItineraryRepository itineraryRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleModelService = vehicleModelService;
        this.routeRepository = routeRepository;
        this.itineraryRepository = itineraryRepository;
    }

    public void saveVehicle(VehicleDto vehicleDto, LoggedUserAccount loggedUserAccount) {
        VehicleModel vehicleModel = vehicleModelService.findVehicleModelById(vehicleDto.getVehicleModelId());

        if (plateNumberRegistered(vehicleDto.getPlateNumber(), loggedUserAccount)) {
            throw new ResourceConflictException("Placa já registrada para outro veículo!");
        }

        Vehicle vehicle = Vehicle.builder()
                .vehicleId(UUID.randomUUID().toString())
                .plateNumber(vehicleDto.getPlateNumber())
                .capacity(vehicleDto.getCapacity())
                .vehicleModel(vehicleModel)
                .active(true)
                .company(new Company(loggedUserAccount.companyId()))
                .build();

        vehicleRepository.save(vehicle);
    }

    public VehiclePresenterList listVehicles(int page, int size, LoggedUserAccount loggedUserAccount) {
        List<VehiclePresenter> vehicles = vehicleRepository.findAllByCompany_CompanyId(loggedUserAccount.companyId(), PageRequest.of(page, size)).stream().map(Vehicle::toPresenter).toList();
        int count = vehicleRepository.countAllByCompany_CompanyId(loggedUserAccount.companyId());
        return new VehiclePresenterList(count, vehicles);
    }

    public List<VehiclePresenter> listAllVehicles(LoggedUserAccount loggedUserAccount) {
        return vehicleRepository.findAllByCompany_CompanyId(loggedUserAccount.companyId()).stream().map(Vehicle::toPresenter).toList();
    }

    public List<VehiclePresenter> listAllActiveVehicles(LoggedUserAccount loggedUserAccount) {
        return vehicleRepository.findAllByCompany_CompanyIdAndActiveTrue(loggedUserAccount.companyId()).stream().map(Vehicle::toPresenter).toList();
    }

    public Vehicle findVehicleById(String vehicleId, LoggedUserAccount loggedUserAccount) {
        return vehicleRepository.findByVehicleIdAndCompany_CompanyId(vehicleId, loggedUserAccount.companyId()).orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));
    }

    public VehiclesStatsPresenter getVehiclesStats(LoggedUserAccount loggedUserAccount) {
        int total = vehicleRepository.countAllByCompany_CompanyId(loggedUserAccount.companyId());
        int active = vehicleRepository.countAllByCompany_CompanyIdAndActive(loggedUserAccount.companyId(), true);
        int inactive = vehicleRepository.countAllByCompany_CompanyIdAndActive(loggedUserAccount.companyId(), false);

        return new VehiclesStatsPresenter(total, active, inactive);
    }

    public void updateVehicle(String vehicleId, VehicleDto vehicleDto, LoggedUserAccount loggedUserAccount) {
        Vehicle vehicle = findVehicleById(vehicleId, loggedUserAccount);

        if (!vehicleDto.getPlateNumber().equals(vehicle.getPlateNumber())) {
            if (plateNumberRegistered(vehicleDto.getPlateNumber(), loggedUserAccount)) {
                throw new ResourceConflictException("Placa já registrada para outro veículo");
            }

            vehicle.setPlateNumber(vehicleDto.getPlateNumber());
        }

        vehicle.setCapacity(vehicleDto.getCapacity());

        vehicleRepository.save(vehicle);
    }

    public boolean plateNumberRegistered(String plateNumber, LoggedUserAccount loggedUserAccount) {
        return vehicleRepository.findByPlateNumberAndCompany_CompanyId(plateNumber, loggedUserAccount.companyId()).isPresent();
    }

    public void enableVehicle(String vehicleId, LoggedUserAccount loggedUserAccount) {
        Vehicle vehicle = findVehicleById(vehicleId, loggedUserAccount);
        vehicle.enable();
        vehicleRepository.save(vehicle);
    }

    public void disableVehicle(String vehicleId, LoggedUserAccount loggedUserAccount) {
        Vehicle vehicle = findVehicleById(vehicleId, loggedUserAccount);

        List<Route> route = routeRepository
                .findAllRoutesByCompany_CompanyIdAndDefaultVehicle_VehicleId(loggedUserAccount.companyId(), vehicleId);

        if (!route.isEmpty()) {
            throw new ResourceConflictException("Não foi possível desabilitar o veículo! Esse veículo está cadastrado como veículo padrão para uma rota.");
        }

        List<Itinerary> itineraries = itineraryRepository
                .findAllScheduledAndOngoingItinerariesByVehicleId(loggedUserAccount.companyId(), vehicleId);

        if (!itineraries.isEmpty()) {
            throw new ResourceConflictException("Não foi possível desabilitar o veículo! Esse veículo está sendo utilizado em itinerários com o status AGENDADO ou EM ANDAMENTO.");
        }

        vehicle.disable();
        vehicleRepository.save(vehicle);
    }

}
