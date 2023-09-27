package com.progettobase.progettobackend.entity;

public @interface Email {

    String message() default "Il campo email deve essere un indirizzo email valido";


}
