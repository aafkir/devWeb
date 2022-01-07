package fr.umontpellier.service;
import java.util.Collection;
import java.util.List;

import packageTestHibernate.Lieu;
import packageTestHibernate.Monument;
public interface IServiceListeMonument {
	
boolean save(Monument monument) throws Exception	;
Collection<Monument> findByNom (String nom)throws Exception ;
Collection<Monument> findByGeohach (String geohash)throws Exception ;

Iterable<Monument> findAll();

Iterable<Lieu> findAllLieu();
}
