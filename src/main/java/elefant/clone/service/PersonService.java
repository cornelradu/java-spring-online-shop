package elefant.clone.service;

import elefant.clone.model.Person;
import elefant.clone.model.Roles;
import elefant.clone.repository.PersonRepository;
import elefant.clone.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean PersonExists(String name){

        Person p = this.personRepository.readByName(name);

        if(p == null){
            return  false;
        }
        return true;
    }

    public boolean createNewPerson(Person person){
        boolean isSaved = false;
        if(PersonExists(person.getName())) return false;
        Roles role = rolesRepository.getByRoleName("User");
        person.setRoles(role);
        person.setPwd(passwordEncoder.encode(person.getPwd()));
        person = personRepository.save(person);
        if (null != person && person.getId() > 0)
        {
            isSaved = true;
        }
        return isSaved;
    }
}

