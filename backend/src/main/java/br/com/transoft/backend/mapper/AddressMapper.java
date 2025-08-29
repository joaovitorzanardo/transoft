package br.com.transoft.backend.mapper;

import br.com.transoft.backend.dto.address.AddressDto;
import br.com.transoft.backend.entity.Address;
import br.com.transoft.backend.entity.Coordinate;

public class AddressDtoToEntityMapper {

    public static Address convert(AddressDto addressDto, Coordinate coordinate) {
        return Address.builder()
                .cep(addressDto.getCep())
                .street(addressDto.getStreet())
                .district(addressDto.getDistrict())
                .number(addressDto.getNumber())
                .complement(addressDto.getComplement())
                .city(addressDto.getCity())
                .uf(addressDto.getUf())
                .coordinate(coordinate)
                .build();
    }

}
