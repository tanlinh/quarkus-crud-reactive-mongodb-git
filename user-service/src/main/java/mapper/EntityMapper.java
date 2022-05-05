package mapper;

public interface EntityMapper<D, E>{

    D toDTO(E entity);

    E toEntity(D dto);
}
