package com.base.spring.service.querydsl;

import com.base.spring.repository.querydsl.QUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016-02-27
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
//http://www.baeldung.com/intro-to-querydsl
@Service
@Transactional
public class QUserService {

    @Autowired
    private QUserRepository qUserRepository;

    /**
     * Find customer whose name equal to the name provided.
     * @param name
     * @return

    public UserEntity getUserByName(String name) {

        QUserEntity user =  QUserEntity.userEntity;
        Predicate predicate = user.userName.eq(name);

        return qUserRepository.findOne(predicate);
    }*/

    /**
     * Get all customer whose country is provided.
     * @param country
     * @return

    public List<UserEntity> getCustomerFrom(String country) {
        List<Customer> customers = new ArrayList<UserEntity>();
        // use join expression any(). Restriction on the join.
        // For multiple restriction use JPASubQuery().
        BooleanExpression isCustomerCountry = QCustomer.customer.country.name.eq(country);

        Iterator<Customer> itr = this.customerRepository.findAll(isCustomerCountry).iterator();
        while(itr.hasNext())
            customers.add(itr.next());

        return customers;
    }

    public List<Customer> getCustomerAgeGroup(int startAge, int endAge) {
        List<Customer> customers = new ArrayList<Customer>();
        QCustomer customer = QCustomer.customer;
        Predicate cus = customer.age.between(startAge, endAge);

        Iterator<Customer> itr = this.customerRepository.findAll(cus).iterator();
        while(itr.hasNext())
            customers.add(itr.next());

        return customers;
    }*/

    /**
     * Get all customer sorted by name of the customer.
     * @return

    public List<Customer> getAllCustomer() {
        List<Customer> customers = new ArrayList<Customer>();

        OrderSpecifier<String> sortAsc = QCustomer.customer.name.asc();
        Iterator<Customer> itr = this.customerRepository.findAll(sortAsc).iterator();
        while(itr.hasNext())
            customers.add(itr.next());
        return customers;
    }

    public List<String> getAllCountries() {
        List<String> countries= new ArrayList<String>();

        OrderSpecifier<String> sortAsc = QCountry.country.name.asc();
        Iterable<Country> fetchCountries = this.countryRepository.findAll(sortAsc);
        Iterator<Country> itr = fetchCountries.iterator();
        while(itr.hasNext()){
            countries.add(itr.next().getName());
        }

        return countries;
    }*/
}
