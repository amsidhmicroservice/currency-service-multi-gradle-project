package com.amsidh.mvc.currencyexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amsidh.mvc.currencyexchange.entity.Exchange;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long>, JpaSpecificationExecutor<Exchange> {
    @Query(name = "findExchangeByCurrencyFromAndCurrencyTo", value = "from Exchange where currencyFrom=:currencyFrom and currencyTo=:currencyTo")
    Exchange findExchangeByCurrencyFromAndCurrencyTo(@Param("currencyFrom") String currencyFrom, @Param("currencyTo") String currencyTo);
}
