package com.progettobase.progettobackend.service;

import com.progettobase.progettobackend.entity.AdminDB;

public interface AdminService {
    public AdminDB findAdminDBByUsername(String username);

    public AdminDB findAdminDBByEmail(String email);
}
