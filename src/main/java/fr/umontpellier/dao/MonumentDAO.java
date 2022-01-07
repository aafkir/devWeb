package fr.umontpellier.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import packageTestHibernate.Monument;

@Component
public class MonumentDAO implements IMonumentDAO{
	@Autowired
	MonumentRepository monumentRepository;
	public boolean save(Monument monument) throws Exception{
		monumentRepository.save(monument);
		return false;
	}
	
}
