"""
Desenvolva um programa contendo uma função que permita ao usuário solicitar o PIB de um país para um determinado ano. 
O programa solicita ao usuário o nome do país e o ano desejado.
Caso o país solicitado ou o ano não sejam válidos, o programa deve informar, na saída, a mensagem: 

País não disponível.
ou
Ano não disponível.

a depender do tipo de dado não encontrado. 
"""

import csv

lista_paises = []
lista_checar_paises = []

pais_usuario = input('Informe um país: ')

with open("pib.csv", 'r', encoding="utf8") as tabela_csv:
    reader = csv.DictReader(tabela_csv)
    for row in reader:
        lista_paises.append(dict(row))

for dicionario in lista_paises:
    lista_checar_paises.append(dicionario['País'])


# Função para checar encontrar o país, depois o ano e mostrar os valores
def exibir_pib_escolhido(pais_usuario, ano_usuario):
    for dicionario in lista_paises:
        if pais_usuario in dicionario.values():
            if ano_usuario in dicionario.keys():
                print(
                    f"O PIB do(a) {dicionario['País']} em {ano_usuario} foi: US$ {dicionario[ano_usuario]} trilhões.")
            else:
                print('Ano não disponível')


# Função para checar se o país está na lista
def checar_e_exibir_pib(pais_usuario):
    if pais_usuario in lista_checar_paises:
        ano_usuario = input('Informe um ano entre 2013 e 2020: ')
        exibir_pib_escolhido(pais_usuario, ano_usuario)
    else:
        print('País não disponível.')


checar_e_exibir_pib(pais_usuario)
