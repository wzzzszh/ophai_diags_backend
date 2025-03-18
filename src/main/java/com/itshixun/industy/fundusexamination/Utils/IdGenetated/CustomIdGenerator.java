package com.itshixun.industy.fundusexamination.Utils.IdGenetated;

import com.itshixun.industy.fundusexamination.Utils.IdGenetated.IdGenerator;
import com.itshixun.industy.fundusexamination.Utils.IdGenetated.PrefixType;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import java.io.Serializable;
import java.util.UUID;

public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String entityClassName = object.getClass().getSimpleName();
        PrefixType prefixType = determinePrefixType(entityClassName);
        return IdGenerator.generateId(prefixType);
    }

    private PrefixType determinePrefixType(String entityClassName) {
        switch(entityClassName) {
            case "User": return PrefixType.USER;
            case "Patient": return PrefixType.PATIENT;
            default: throw new IllegalArgumentException("Unsupported entity type");
        }
    }
}