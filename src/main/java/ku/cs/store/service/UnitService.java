package ku.cs.store.service;

import ku.cs.store.entity.Category;
import ku.cs.store.entity.Unit;
import ku.cs.store.repository.ProductRepository;
import ku.cs.store.repository.UnitRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitService {
    @Autowired
    private ProductRepository productRepository;
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
    public void deleteUnitById(Long unitId){
        // ตรวจสอบว่าไม่มีสินค้าที่อ้างอิงถึงหน่วยนับนี้
        if (!productRepository.existsByUnitId(unitId)) {
            // ไม่มีสินค้าที่อ้างอิงถึงหน่วยนับนี้ จึงสามารถลบหน่วยนับได้
            unitRepository.deleteById(unitId);
        } else {
            // มีสินค้าที่อ้างอิงถึงหน่วยนับนี้ ไม่สามารถลบ
            throw new RuntimeException("Cannot delete unit with associated products.");
        }
    }
    public void editUnit(Long id,Unit updateUnit){
        Unit existingUnit = unitRepository.findById(id).orElseThrow();
        if(!updateUnit.getName().equals(existingUnit.getName())){
            existingUnit.setName(updateUnit.getName());
            existingUnit.setQuantity(updateUnit.getQuantity());

        }
        unitRepository.save(existingUnit);
    }
    public boolean unitNameIsExisted(Unit unit) {
        Optional<Unit> existingUnit = unitRepository.findByName(unit.getName());
        return existingUnit.isPresent();
    }

}
