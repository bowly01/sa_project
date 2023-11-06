package ku.cs.store.service;

import ku.cs.store.entity.Category;
import ku.cs.store.entity.Product;
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
        // ตรวจสอบว่าข้อมูลในตาราง product มีการอ้างอิงถึงประเภทที่กำลังจะลบหรือไม่
        List<Product> products = productRepository.findByUnitId(unitId);
        if (!products.isEmpty()) {
            // ข้อมูลในตาราง product มีการอ้างอิงถึงประเภทที่กำลังจะลบ
            // ไม่สามารถลบข้อมูลได้
            return;
        }
        // ลบข้อมูลในตาราง category
        unitRepository.deleteById(unitId);
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
