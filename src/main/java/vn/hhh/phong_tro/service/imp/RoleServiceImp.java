package vn.hhh.phong_tro.service.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.model.Role;
import vn.hhh.phong_tro.repository.RoleRepository;
import vn.hhh.phong_tro.service.RoleService;

@Service
@RequiredArgsConstructor

public class RoleServiceImp implements RoleService {
    final RoleRepository roleRepository;
    @Override
    public Role getByRoleId(Integer Id) {
        return roleRepository.findById(Long.valueOf(Id)).orElse(null);
    }
}
