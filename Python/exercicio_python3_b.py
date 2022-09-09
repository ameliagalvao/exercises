"""
Desenvolva um programa contendo uma função que liste, por país, a estimativa de variação do PIB, em percentual, entre 2013 e 2020.
"""


import csv


def listar_variacao_pib():
    lista_dicionarios = []
    lista_paises = []
    lista_pib_2013 = []
    lista_pib_2020 = []
    lista_variacao_percentual = []

    with open("pib.csv", 'r', encoding="utf8") as tabela_csv:
        reader = csv.DictReader(tabela_csv)
        for row in reader:
            lista_dicionarios.append(dict(row))

    for dicionario in lista_dicionarios:
        lista_paises.append(dicionario['País'])
        lista_pib_2013.append(float(dicionario['2013']))
        lista_pib_2020.append(float(dicionario['2020']))

    for n in range(15):
        variacao = round((
            (lista_pib_2020[n] - lista_pib_2013[n]) / lista_pib_2013[n]) * 100, 2)
        lista_variacao_percentual.append(variacao)

    for n in range(15):
        mensagem_final = f"Variação de {lista_variacao_percentual[n]}% entre 2013 e 2020."
        print(f"{lista_paises[n]:<20}{mensagem_final:>15}")


listar_variacao_pib()
