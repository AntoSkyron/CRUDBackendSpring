package com.progettobase.progettobackend.repository;

import com.progettobase.progettobackend.entity.AdminDB;
import com.progettobase.progettobackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
    public interface AdminRepository extends JpaRepository<AdminDB, Long> {
        AdminDB findAdminDBByUsername(String username);

        AdminDB findAdminDBByEmail(String email);
}
