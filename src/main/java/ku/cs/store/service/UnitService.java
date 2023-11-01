package ku.cs.store.service;

import ku.cs.store.entity.Category;
import ku.cs.store.entity.Unit;
import ku.cs.store.model.ProductRequest;
import ku.cs.store.repository.UnitRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitService {
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private ModelMapper  modelMapper;
    public List<Unit> getAllUnit() {
        return unitRepository.findAll();
    }


    public void createUnit(Unit unit) {
        Unit record = modelMapper.map(unit, Unit.class);
        unitRepository.save(record);
    }
    public Unit getOneById(Long id){return unitRepository.findById(id).get();}
    //not yet
    public void deleteUnit(Unit unit){
        Unit record = modelMapper.map(unit, Unit.class);
        unitRepository.save(record);    }
    public boolean unitNameIsExisted(Unit unit) {
        Optional<Unit> existingUnit = unitRepository.findByName(unit.getName());
        return existingUnit.isPresent();
    }

}
