package com.cozyhome.onlineshop.productservice.service.impl;

import com.cozyhome.onlineshop.dto.MaterialDto;
import com.cozyhome.onlineshop.productservice.model.Material;
import com.cozyhome.onlineshop.productservice.repository.MaterialRepository;
import com.cozyhome.onlineshop.productservice.service.MaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<MaterialDto> getMaterials() {
        List<Material> materials = materialRepository.findAllByActive(true);
        if (materials.isEmpty()) {
            log.info("[ON getColors]:: Materials not found.");
            return new ArrayList<>();
        }
        return materials.stream().map(material -> modelMapper.map(material, MaterialDto.class)).toList();
    }
}
