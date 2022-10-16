# KrutoCompiller

В классе Main нужно изменить переменную "input", задав путь до файла с программой на языке KRUTO.

В качестве языка программирования компилятора используется Java, а для генерации синтаксического дерева фреймворк ANTLR. Целевой код – MIPS.

На вход подаётся текст программы и КС-грамматика. Далее ANTLR разбивает текст на токены, генерирует дерево и классы его обхода. 

С помощью одного из классов обхода дерева генерируется таблица символов и производится семантический анализ. 
Далее, обходя дерево уже новым способом, и используя таблицу символов, генерируется трехадресный код, после чего каждая строка промежуточного кода переводится на язык MIPS ассемблера.

<h2>ANTLR: Лексический и синтаксический анализ</h2>
В своей работе я использовал ANTLR, так как при работе с ним можно сконцентрироваться только на написании КС-грамматики. 
Он заменяет тысячи строк кода, описывающих грамматику в условных операторах и построение АСТ. 
К тому-же он просто устанавливается и с ним удобно работать. 

В ходе работы ANTLR создаёт следующие классы:
* KrutoLexer - это описание класса лексического анализатора, отвечающего грамматике KrutoLexer.
* KrutoParser - это описание класса синтаксического анализатора, отвечающего грамматике KrutoParser.
* KrutoParserListener.java, KrutoParserBaseListener.java, KrutoParserVisitor, KrutoParserBaseVisitor — это классы, содержащие описания методов, которые позволяют выполнять определенные действия при обходе синтаксического дерева.
* KrutoParser.tokens, KrutoLexer.tokens — это вспомогательные классы, которые содержат информацию о токенах. 

<h2>ANTLR: КС-грамматика</h2>
Грамматику для удобства я разделил на грамматики для лексера и парсера. 
За основу была взята КС-грамматика языка Pascal, в которую было много чего добавлено, например операторы циклов, принт, ретурн. 
Много чего переделано, например вызов функции и передача аргументов.

<h2>Обход дерева. Генерация таблицы символов</h2>
Обход дерева на этом этапе был проведен с помощью написанного класса HelloWalker, расширяющего KrutoParserBaseListener. Он автоматически обходит дерево с помощью DFS и реагирует на распознавание интересующих классов (a.k.a правил грамматики). Например, при попадании в класс VariableDeclarationPart (там объявляются переменные), создаются записи в таблице символов, а также проверяется не объявлена ли переменная ранее.

Для каждой функции создается своя таблица символов. Глобальная таблица представляет собой HashMap, в которой ключ – название функции, а значение – локальная таблица символов, которая также является объектом класса HashMap. Структура локальной таблицы символов представлена на слайде.

<h2>Обход дерева. Семантический анализ </h2>
Также в классе HelloWalker имеется проверка логики программы. Компилятор прекращает выполнение программы и выводит сообщение об ошибке, если:
* Объявляется уже ранее объявленная переменная или функция,
* Используется необъявленная переменная или функция,
* В качестве названия функции используется слово «main»,
* Ретюрн возвращает переменную не с тем типом, который объявлен в функции
* Или имеется ретюрн, но функция возвращает void.

<h2>Генерация промежуточного кода</h2>
В роли промежуточного кода я выбрал трехадресный код, включающий в себя метки функций, циклов и т.п. Трехадресный код генерируется в классе ThreeAddressWalker. Это уже мой собственный класс, который обходит дерево рекурсивным алгоритмом поиска в глубину. Когда мы узнаём, что текущий узел создан на основе интересующего нас класса, например AssignmentStatementContext (используя instanceof), тогда относительно этого узла реализуем алгоритм поиска в ШИРИНУ, а когда заканчиваем поиск в ширину, детей AssignmentStatement далее не посещаем и ДФС просто продолжает свою работу. С помощью такого способа сохраняется очередность выражений. 

Соответственно при парсинге дерева создаётся трехадресный код. Lw – это запись переменной во временный регистр, li – запись числа. T8 и t7 на слайде не расписаны, из-за того, что дерево не влезло. Param – это аргумент передающийся в функцию, вызов которой эквиваленен команде rc (от слов returned call) - временная переменная – название функции – кол-во ее аргументов. Sw – запись временной переменной в переменную из таблицы символов.

И еще смотрите, когда мы начинаем парсить выражение, мы смотрим если оно приравнивается переменной с типом флоат, то тогда все временные переменные будут начинаться на f, а иначе на t. Но, когда мы доходим до функции, мы смотрим типы ее переменных, если они инт, то f заменяем на t, и далее эти переменные будут складываться, умнажаться также с t.

Упомяну, что все временные переменные уникальны по индексу. Также присутствуют метки, использующиеся в операциях сравнения, они тоже уникальны. Трехадресный код записывается в файл out_tac.txt

<h2>Трансляция в целевой код</h2>
В качестве целевого кода я использую MIPS ассемблер. Я выбрал его, так как он считается языком, который изучают в вузах, это RISC архитектура, он проще x86, у него обширное комьюнити (в основном англоязычное) со множеством обучающего материала. И как оказалось с выбором я не ошибся, он реально хорош. 

Но вот что меня подвело, так это выбор симулятора, а именно MARS. Например, в нем нельзя сразу загрузить число в флоат регистр в текст сегменте, пришлось танцевать с бубном.

Для генерации целевого кода и записи его в файл .asm я использую класс Target, в котором используются трехадресный код и таблица символов. Сначала создаётся сегмент data, в который записываются все переменные из таблицы символов, плюс пара нужных для работы переменных. Далее объявляется text сегмент, сразу же после которого записывается команда для прыжка на метку main. 

После этого происходит транслирование трехадресного кода в ассемблер. Открыть asm файл нужно с помощью MARS MIPS. 
