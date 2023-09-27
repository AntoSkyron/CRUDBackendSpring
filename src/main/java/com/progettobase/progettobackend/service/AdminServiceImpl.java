package com.progettobase.progettobackend.service;

import com.progettobase.progettobackend.entity.AdminDB;
import com.progettobase.progettobackend.repository.AdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public AdminDB findAdminDBByUsername(String username){
        return adminRepository.findAdminDBByUsername(username);
    }

    public AdminDB findAdminDBByEmail(String email){
        return adminRepository.findAdminDBByEmail(email);
    }


}
