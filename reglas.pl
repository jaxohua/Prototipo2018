%Se definen los tipos de objetos

pertenece(X, [X|_]).
pertenece(X, [_|Ys]):- pertenece(X, Ys).


es_bd(X):- X is 1.
es_relacion(X):- pertenece(X,[6,7,8,9,10,11,12,13,14,15]).
es_atributo(X):-pertenece(X,[16,17,18,19,20,21,22,23,24,25]).
es_valor(X):-pertenece(X,[26,27,28,29,30,31,32,33,34,35]).
es_oper_rel(X):-pertenece(X,[36,37,38,39,40,41,42,43,44,45]).
es_oper_comp(X):-pertenece(X,[46,47,48,49,50,51,52,53,54,55]).

% dos operadores relacionales
se_pueden_tocar(X,Y):-es_oper_rel(X),es_oper_rel(Y). 

 %operador relacional y relacion.
se_pueden_tocar(X,Y):-es_oper_rel(X),es_relacion(Y); 
					es_oper_rel(Y),es_relacion(X). 

%operador relacional y atributo (formar condición)
se_pueden_tocar(X,Y):-es_oper_rel(X),es_atributo(Y);
						es_oper_rel(Y),es_atributo(X). 

%Relacion y atributo para asignar valor de atributo.
se_pueden_tocar(X,Y):-es_relacion(X),es_atributo(Y);   
						es_relacion(Y),es_atributo(X). 

%atributo con atributo para formar condición del operador.
se_pueden_tocar(X,Y):-es_atributo(X),es_atributo(Y);	
						es_atributo(Y),es_atributo(X).

%atributo y operador de comparación para para condición de operador
se_pueden_tocar(X,Y):-es_atributo(X),es_oper_comp(Y);	
						es_atributo(Y),es_oper_comp(X). 

%atributo y valor
se_pueden_tocar(X,Y):-es_atributo(X),es_valor(Y);
						es_atributo(Y),es_valor(X).

%comparacion y valor
se_pueden_tocar(X,Y):-es_oper_comp(X),es_valor(Y);
						es_oper_comp(Y),es_valor(X).
