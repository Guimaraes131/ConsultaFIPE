package br.com.alura.ConsultaFIPE.main;

import br.com.alura.ConsultaFIPE.model.Dados;
import br.com.alura.ConsultaFIPE.model.Modelos;
import br.com.alura.ConsultaFIPE.model.Veiculo;
import br.com.alura.ConsultaFIPE.service.ConsumoAPI;
import br.com.alura.ConsultaFIPE.service.ConversorDados;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
        private final ConsumoAPI consumo = new ConsumoAPI();
        private ConversorDados conversor = new ConversorDados();
        private final Scanner sc = new Scanner(System.in);

        private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void exibeMenu() throws IOException, InterruptedException {

        var menu = """
                    -=-=-= OPÇÕES -=-=-=
                    Carro
                    Moto
                    Caminhão
                    
                    Digite uma das opções para consultar:
                    """;

        System.out.print(menu);

        String veiculoEscolhido = sc.nextLine().toLowerCase();
        String extensaoUrl = verificaVeiculo(veiculoEscolhido);
        String url = URL_BASE + extensaoUrl;
        String json = consumo.obterDados(url);

        System.out.println(json);

        var marcas = conversor.obterLista(json, Dados.class);

        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.print("Informe o código da marca para consulta: ");
        var codigoMarca = sc.nextLine();

        url = url + "/" + codigoMarca + "/modelos";
        json = consumo.obterDados(url);

        var modeloLista = conversor.converterDados(json, Modelos.class);

        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.print("Digite um trecho do nome do carro a ser buscado: ");

        var nomeVeiculo = sc.nextLine().toLowerCase();
        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados:");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("\nDigite o código do carro a ser consultado");
        var codigoCarro = sc.nextLine();

        url = url + "/" + codigoCarro + "/anos";
        json = consumo.obterDados(url);
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (Dados ano : anos) {
            var enderecoAnos = url + "/" + ano.codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.converterDados(json, Veiculo.class);

            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veículos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);
    }


    static String verificaVeiculo(String veiculo) {

        if (veiculo.contains("carr")) {
            return "carros/marcas";
        } else if (veiculo.contains("mot")) {
            return "motos/marcas";
        }

        return "caminhoes/marcas";
    }
}

