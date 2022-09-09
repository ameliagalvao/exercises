"""
Desenvolva uma função que, para um país, exiba o gráfico da evolução do PIB ao longo dos anos.
A função deve receber, como entrada, o nome de um país, e exibir o gráfico para todo o período listado na tabela.
O gráfico deve conter os valores do PIB no eixo das ordenadas (vertical) e os anos no eixo das abscissas (horizontal)
"""


import csv
import matplotlib.pyplot as plt


def criar_grafico_pib_pais(pais_usuario):

    dicionario_paises = []
    lista_anos_escolhido = []
    lista_valores_escolhido = []

    pais_usuario = input('Informe um país: ')

    with open("pib.csv", 'r', encoding="utf8") as tabela_csv:
        reader = csv.DictReader(tabela_csv)
        for row in reader:
            dicionario_paises.append(dict(row))

    for dicionario in dicionario_paises:
        if pais_usuario in dicionario.values():
            dicionario_pais_escolhido = dicionario
            pais_escolhido = dicionario_pais_escolhido.pop('País')

    # Loop para colocar num array os anos do país escolhido
    for ano in dicionario_pais_escolhido:
        lista_anos_escolhido.append(ano)

    # Loop para colocar num array os valores do pib do país escolhido:
    for ano in dicionario_pais_escolhido:
        lista_valores_escolhido.append(dicionario_pais_escolhido[ano])

    print(
        f"A evolução do PIB no(a) {pais_escolhido} entre {lista_anos_escolhido[0]} a {lista_anos_escolhido[-1]} foi:\n")

    eixo_x = lista_anos_escolhido
    eixo_y = lista_valores_escolhido

    plt.plot(eixo_x, eixo_y, '--o')
    plt.xlabel('Ano')
    plt.ylabel('Valor do PIB em trilhões de US$')

    plt.show()


criar_grafico_pib_pais(pais_usuario)
