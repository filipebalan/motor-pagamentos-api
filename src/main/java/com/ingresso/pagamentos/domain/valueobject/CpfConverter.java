package com.ingresso.pagamentos.domain.valueobject;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// O autoApply = true avisa o Spring para usar esse tradutor em TODAS as entidades que tiverem um atributo do tipo Cpf
@Converter(autoApply = true)
public class CpfConverter implements AttributeConverter<Cpf, String> {

    @Override
    public String convertToDatabaseColumn(Cpf attribute) {
        // De Java para Banco de Dados: Extrai a String limpa
        return attribute == null ? null : attribute.getNumero();
    }

    @Override
    public Cpf convertToEntityAttribute(String dbData) {
        // De Banco de Dados para Java: Reconstrói o Objeto de Valor validado
        return dbData == null ? null : new Cpf(dbData);
    }
}