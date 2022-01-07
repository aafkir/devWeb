package fr.umontpellier.dao;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import packageTestHibernate.Lieu;
import packageTestHibernate.Monument;

@Repository
public interface MonumentRepository  {
	boolean save(Monument monument)throws Exception;
	Collection<Monument> findByNom (String nom) ;

}
