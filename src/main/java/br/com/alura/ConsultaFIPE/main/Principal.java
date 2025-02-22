package br.com.alura.ConsultaFIPE.main;

import br.com.alura.ConsultaFIPE.service.ConsumoAPI;
import br.com.alura.ConsultaFIPE.service.ConversorDados;

import java.io.IOException;
import java.util.Scanner;

public class Principal {
        private ConsumoAPI consumo = new ConsumoAPI();
        private ConversorDados conversor = new ConversorDados();
        private Scanner sc = new Scanner(System.in);

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
        System.out.print("Escolha uma marca de acordo com o código: ");

        String marca = sc.nextLine();
        String urlMarcas = url + "/" + marca + "/modelos";

        System.out.println(consumo.obterDados(urlMarcas));

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

