package br.com.alura.ConsultaFIPE.service;

public interface IConversorDados {
    <T> T converterDados(String json, Class<T> tClass);
}
