package br.com.alura.ConsultaFIPE.service;

import java.util.List;

public interface IConversorDados {
    <T> T converterDados(String json, Class<T> tClass);

    <T> List<T> obterLista(String json, Class<T> tClass);
}
