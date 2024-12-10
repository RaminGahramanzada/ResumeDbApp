package repositroy.inter;

import model.Country;

import java.util.List;

public interface CountryRepository {
    List<Country> getAll();

    public Country getById(int id);

    boolean updateCountry(Country u);

    boolean removeCountry(int id);
}
