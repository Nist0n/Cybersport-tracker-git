# -*- coding: utf-8 -*-
"""Сборка отчёта по практическим работам № 3-4 (Cybersport Tracker)."""

import os

from docx import Document
from docx.enum.section import WD_SECTION
from docx.enum.style import WD_STYLE_TYPE
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.enum.text import WD_ALIGN_PARAGRAPH, WD_LINE_SPACING
from docx.oxml import OxmlElement
from docx.oxml.ns import qn
from docx.shared import Cm, Pt, RGBColor

BASE = os.path.dirname(os.path.abspath(__file__))
SHOTS = os.path.join(BASE, "screenshots")
OUT = os.path.join(BASE, "Отчет_ПР3_ПР4_Cybersport_Tracker.docx")

ACCENT = RGBColor(0x2F, 0x63, 0xCC)
DARK = RGBColor(0x1F, 0x29, 0x37)
GREY = RGBColor(0x5B, 0x66, 0x77)

doc = Document()

# ---------------------------------------------------------------- page setup
section = doc.sections[0]
section.page_width = Cm(21.0)
section.page_height = Cm(29.7)
section.top_margin = Cm(2.0)
section.bottom_margin = Cm(2.0)
section.left_margin = Cm(3.0)
section.right_margin = Cm(1.5)

styles = doc.styles

normal = styles["Normal"]
normal.font.name = "Times New Roman"
normal.font.size = Pt(12)
normal.paragraph_format.line_spacing = 1.5
normal.paragraph_format.space_after = Pt(6)
normal.paragraph_format.first_line_indent = Cm(1.25)
normal.paragraph_format.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
normal.element.rPr.rFonts.set(qn("w:eastAsia"), "Times New Roman")

for name, size, color, before, after in (
        ("Heading 1", 16, ACCENT, 18, 8),
        ("Heading 2", 14, DARK, 14, 6),
        ("Heading 3", 12.5, DARK, 10, 4)):
    st = styles[name]
    st.font.name = "Times New Roman"
    st.font.size = Pt(size)
    st.font.bold = True
    st.font.color.rgb = color
    st.paragraph_format.space_before = Pt(before)
    st.paragraph_format.space_after = Pt(after)
    st.paragraph_format.keep_with_next = True
    st.paragraph_format.line_spacing = 1.2
    st.paragraph_format.alignment = WD_ALIGN_PARAGRAPH.LEFT
    st.paragraph_format.first_line_indent = Cm(0)

for name in ("List Bullet", "List Number"):
    st = styles[name]
    st.font.name = "Times New Roman"
    st.font.size = Pt(12)
    st.paragraph_format.line_spacing = 1.5
    st.paragraph_format.space_after = Pt(3)
    st.paragraph_format.alignment = WD_ALIGN_PARAGRAPH.LEFT

code_style = styles.add_style("CodeBlock", WD_STYLE_TYPE.PARAGRAPH)
code_style.base_style = styles["Normal"]
code_style.font.name = "Consolas"
code_style.font.size = Pt(8.5)
code_style.paragraph_format.line_spacing = 1.0
code_style.paragraph_format.space_before = Pt(4)
code_style.paragraph_format.space_after = Pt(8)
code_style.paragraph_format.left_indent = Cm(0.6)
code_style.paragraph_format.right_indent = Cm(0.2)
code_style.paragraph_format.first_line_indent = Cm(0)
code_style.paragraph_format.alignment = WD_ALIGN_PARAGRAPH.LEFT
code_style.paragraph_format.keep_together = True


def set_cell_shading(cell, color):
    tc_pr = cell._tc.get_or_add_tcPr()
    shd = OxmlElement("w:shd")
    shd.set(qn("w:val"), "clear")
    shd.set(qn("w:color"), "auto")
    shd.set(qn("w:fill"), color)
    tc_pr.append(shd)


def add_page_number_footer(section):
    footer = section.footer
    paragraph = footer.paragraphs[0]
    paragraph.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = paragraph.add_run()
    fld_begin = OxmlElement("w:fldChar")
    fld_begin.set(qn("w:fldCharType"), "begin")
    instr = OxmlElement("w:instrText")
    instr.set(qn("xml:space"), "preserve")
    instr.text = "PAGE"
    fld_end = OxmlElement("w:fldChar")
    fld_end.set(qn("w:fldCharType"), "end")
    run._r.append(fld_begin)
    run._r.append(instr)
    run._r.append(fld_end)
    run.font.name = "Times New Roman"
    run.font.size = Pt(10)


section.different_first_page_header_footer = True
add_page_number_footer(section)


def para(text, bold=False, italic=False, size=None, color=None, align=None,
         first_line=True, space_after=6):
    p = doc.add_paragraph()
    if align is not None:
        p.alignment = align
    if not first_line:
        p.paragraph_format.first_line_indent = Cm(0)
    run = p.add_run(text)
    run.bold = bold
    run.italic = italic
    if size:
        run.font.size = Pt(size)
    if color:
        run.font.color.rgb = color
    p.paragraph_format.space_after = Pt(space_after)
    return p


def bullet(text):
    p = doc.add_paragraph(text, style="List Bullet")
    p.paragraph_format.first_line_indent = Cm(0)
    return p


_number = [0]


def numbered_reset():
    _number[0] = 0


def numbered(text):
    _number[0] += 1
    p = doc.add_paragraph()
    p.paragraph_format.left_indent = Cm(1.2)
    p.paragraph_format.first_line_indent = Cm(-0.7)
    p.paragraph_format.line_spacing = 1.5
    p.paragraph_format.space_after = Pt(3)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    p.add_run(f"{_number[0]}. {text}")
    return p


def caption(text):
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p.paragraph_format.space_before = Pt(2)
    p.paragraph_format.space_after = Pt(10)
    run = p.add_run(text)
    run.italic = True
    run.font.size = Pt(10)
    run.font.color.rgb = GREY


def add_image(filename, width_cm=7.0, cap=None):
    path = os.path.join(SHOTS, filename)
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p.paragraph_format.space_before = Pt(4)
    p.paragraph_format.space_after = Pt(2)
    p.paragraph_format.keep_with_next = True
    p.add_run().add_picture(path, width=Cm(width_cm))
    if cap:
        caption(cap)


def add_code(code, listing_caption=None):
    if listing_caption:
        p = doc.add_paragraph()
        p.paragraph_format.space_before = Pt(8)
        p.paragraph_format.space_after = Pt(2)
        p.paragraph_format.keep_with_next = True
        run = p.add_run(listing_caption)
        run.italic = True
        run.font.size = Pt(10)
        run.font.color.rgb = GREY
    p = doc.add_paragraph(style="CodeBlock")
    run = p.add_run(code.strip("\n"))
    run.font.name = "Consolas"
    run.font.size = Pt(8.5)
    p_pr = p._p.get_or_add_pPr()
    shd = OxmlElement("w:shd")
    shd.set(qn("w:val"), "clear")
    shd.set(qn("w:color"), "auto")
    shd.set(qn("w:fill"), "F3F5F8")
    p_pr.append(shd)
    borders = OxmlElement("w:pBdr")
    for edge in ("top", "left", "bottom", "right"):
        el = OxmlElement("w:" + edge)
        el.set(qn("w:val"), "single")
        el.set(qn("w:sz"), "4")
        el.set(qn("w:space"), "4")
        el.set(qn("w:color"), "D7DCE5")
        borders.append(el)
    p_pr.append(borders)


def _row_properties(row):
    tr_pr = row._tr.get_or_add_trPr()
    tr_pr.append(OxmlElement("w:cantSplit"))
    return tr_pr


def add_table(headers, rows, widths=None, font_size=10.5, header_fill="E8EEF9"):
    table = doc.add_table(rows=1, cols=len(headers))
    table.style = "Table Grid"
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table.autofit = True
    header_row = table.rows[0]
    _row_properties(header_row)
    header_row._tr.get_or_add_trPr().append(OxmlElement("w:tblHeader"))
    hdr = header_row.cells
    for i, text in enumerate(headers):
        hdr[i].text = ""
        p = hdr[i].paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p.paragraph_format.first_line_indent = Cm(0)
        p.paragraph_format.line_spacing = 1.0
        run = p.add_run(text)
        run.bold = True
        run.font.size = Pt(font_size)
        run.font.name = "Times New Roman"
        set_cell_shading(hdr[i], header_fill)
    for row in rows:
        table_row = table.add_row()
        _row_properties(table_row)
        cells = table_row.cells
        for i, text in enumerate(row):
            cells[i].text = ""
            p = cells[i].paragraphs[0]
            p.alignment = WD_ALIGN_PARAGRAPH.LEFT
            p.paragraph_format.first_line_indent = Cm(0)
            p.paragraph_format.line_spacing = 1.0
            p.paragraph_format.space_after = Pt(2)
            run = p.add_run(str(text))
            run.font.size = Pt(font_size)
            run.font.name = "Times New Roman"
    if widths:
        for row in table.rows:
            for i, width in enumerate(widths):
                row.cells[i].width = Cm(width)
    doc.add_paragraph().paragraph_format.space_after = Pt(2)
    return table


# ---------------------------------------------------------------- title page
para("МИНОБРНАУКИ РОССИИ", bold=True, size=12, align=WD_ALIGN_PARAGRAPH.CENTER, first_line=False, space_after=2)
para("Федеральное государственное бюджетное образовательное учреждение\n"
     "высшего образования", size=11, align=WD_ALIGN_PARAGRAPH.CENTER, first_line=False, space_after=2)
para("«МИРЭА — Российский технологический университет»", bold=True, size=12,
     align=WD_ALIGN_PARAGRAPH.CENTER, first_line=False, space_after=8)
para("Институт кибербезопасности и цифровых технологий\n"
     "Кафедра КБ-14 «Цифровые технологии обработки данных»", size=11,
     align=WD_ALIGN_PARAGRAPH.CENTER, first_line=False, space_after=40)

para("ОТЧЁТ", bold=True, size=24, align=WD_ALIGN_PARAGRAPH.CENTER, first_line=False, space_after=4)
para("по практическим работам № 3–4", size=14, align=WD_ALIGN_PARAGRAPH.CENTER, first_line=False, space_after=4)
para("по дисциплине «Разработка мобильных приложений»", size=13,
     align=WD_ALIGN_PARAGRAPH.CENTER, first_line=False, space_after=28)
para("Тема: «Архитектура MVVM, ViewModel, LiveData, MediatorLiveData\n"
     "и списки на RecyclerView в Android-приложении Cybersport Tracker»",
     bold=True, size=14, align=WD_ALIGN_PARAGRAPH.CENTER, first_line=False, space_after=48)

para("Выполнил: студент группы ____________", size=12, align=WD_ALIGN_PARAGRAPH.RIGHT, first_line=False, space_after=2)
para("____________________ / ____________________", size=12, align=WD_ALIGN_PARAGRAPH.RIGHT, first_line=False, space_after=16)
para("Проверил: ____________________", size=12, align=WD_ALIGN_PARAGRAPH.RIGHT, first_line=False, space_after=60)
para("Москва, 2026", size=12, align=WD_ALIGN_PARAGRAPH.CENTER, first_line=False)

doc.add_page_break()

# ---------------------------------------------------------------- contents
doc.add_heading("Содержание", level=1)
contents = [
    "Введение",
    "1 Теоретическая часть",
    "1.1 Архитектурные паттерны MVC, MVP, MVVM, MVI, VIPER",
    "1.2 MVVM и чистая архитектура проекта",
    "1.3 ViewModel: жизненный цикл, ViewModelProvider, Factory",
    "1.4 LiveData и организация реактивного состояния",
    "1.5 MediatorLiveData как средство объединения источников",
    "1.6 RecyclerView: Adapter, ViewHolder, LayoutManager, DiffUtil",
    "2 Практическая работа № 3. ViewModel, LiveData, MediatorLiveData",
    "2.1 Контрольное задание и исходное состояние проекта",
    "2.2 Экранные ViewModel и фабрики",
    "2.3 Обновление интерфейса через LiveData",
    "2.4 Объединение сетевых данных и Room с помощью MediatorLiveData",
    "2.5 Результаты практической работы № 3",
    "3 Практическая работа № 4. RecyclerView и передача данных через LiveData",
    "3.1 Контрольное задание",
    "3.2 Заглушка набора данных в слое data",
    "3.3 Реализация списка команд на RecyclerView",
    "3.4 Передача данных из ViewModel в адаптер",
    "3.5 Состояния списка и результаты работы",
    "4 Тестирование и проверка работоспособности",
    "Заключение",
    "Список использованных источников",
    "Приложение А. Перечень изменённых файлов",
]
for item in contents:
    p = doc.add_paragraph()
    p.paragraph_format.first_line_indent = Cm(0)
    p.paragraph_format.space_after = Pt(2)
    p.paragraph_format.line_spacing = 1.15
    run = p.add_run(item)
    run.font.size = Pt(11)
    if item[0].isdigit() and "." not in item[:3]:
        run.bold = True

doc.add_page_break()

# ---------------------------------------------------------------- intro
doc.add_heading("Введение", level=1)
para("Современное мобильное приложение, работающее с сетевыми данными, локальной базой данных "
     "и несколькими экранами, невозможно поддерживать, если вся логика сосредоточена в Activity. "
     "Поворот экрана, смена конфигурации, повторный вход на экран и обновление данных из базы "
     "должны обрабатываться предсказуемо. Именно этим задачам посвящены практические работы № 3 и № 4 "
     "дисциплины «Разработка мобильных приложений».")
para("В качестве объекта разработки используется учебное Android-приложение Cybersport Tracker — "
     "киберспортивный трекер турниров, команд и матчей. Проект состоит из трёх Gradle-модулей: "
     "app (слой представления), domain (модели, интерфейсы репозиториев и сценарии использования) "
     "и data (реализации репозиториев, Room, имитация сетевого API, SharedPreferences, TFLite). "
     "Приложение написано на Java 11 с использованием AndroidX, Material 3 и Room.")
para("Цель работ — закрепить навыки проектирования Android-приложения по шаблону MVVM, "
     "разделения ответственности между Activity и ViewModel, реактивного обновления интерфейса "
     "через LiveData, объединения нескольких источников данных с помощью MediatorLiveData "
     "и построения производительного списка на RecyclerView.")
para("Для достижения цели были поставлены следующие задачи:", first_line=False)
numbered_reset()
numbered("проанализировать архитектурные шаблоны MVC, MVP, MVVM, MVI и VIPER и обосновать выбор MVVM;")
numbered("вынести взаимодействие Activity со слоем domain в экранные ViewModel;")
numbered("организовать обновление состояния интерфейса исключительно через LiveData;")
numbered("объединить замоканные данные «сетевого» слоя и наблюдаемые данные Room с помощью MediatorLiveData;")
numbered("реализовать отображение списка команд с изображениями через RecyclerView;")
numbered("проверить сборку, авторизацию, гостевой режим, избранное, поиск, фильтрацию и поворот экрана.")
para("Практическая значимость работы состоит в том, что полученная архитектура пригодна для "
     "дальнейшего развития приложения: замены mock-источника на Retrofit, добавления новых экранов "
     "и покрытия ViewModel модульными тестами.")

# ---------------------------------------------------------------- theory
doc.add_heading("1 Теоретическая часть", level=1)
doc.add_heading("1.1 Архитектурные паттерны MVC, MVP, MVVM, MVI, VIPER", level=2)
para("Архитектурный шаблон задаёт правила разделения ответственности между пользовательским "
     "интерфейсом, логикой представления и бизнес-логикой. Чем последовательнее это разделение, "
     "тем проще тестировать код, менять источники данных и переиспользовать экраны. "
     "Рассмотрим пять распространённых подходов.")

doc.add_heading("MVC (Model — View — Controller)", level=3)
para("Model содержит данные и бизнес-логику, View отображает данные и принимает события "
     "пользователя, Controller связывает их: получает события от View, изменяет Model и обновляет "
     "представление. Шаблон прост и исторически популярен в веб-разработке, однако в больших "
     "приложениях контроллер перегружается, а View и Model оказываются связанными слишком тесно.")

doc.add_heading("MVP (Model — View — Presenter)", level=3)
para("В MVP Presenter управляет представлением вручную: View передаёт ему события, Presenter "
     "обращается к Model и вызывает у View методы отображения. View становится пассивным, "
     "а Presenter легко тестируется без Android-инфраструктуры. Недостаток — большое количество "
     "интерфейсов и риск «раздувания» Presenter при росте экрана.")

doc.add_heading("MVVM (Model — View — ViewModel)", level=3)
para("В MVVM View подписывается на ViewModel, а ViewModel хранит состояние экрана и не знает "
     "о конкретной View. В Android это реализуется через androidx.lifecycle.ViewModel и LiveData: "
     "Activity наблюдает за LiveData, а ViewModel переживает пересоздание Activity при повороте "
     "экрана. Двусторонняя привязка данных не обязательна — достаточно одностороннего потока "
     "«состояние → View», что хорошо ложится на Java и XML-разметку.")

doc.add_heading("MVI (Model — View — Intent)", level=3)
para("MVI строит приложение вокруг однонаправленного потока данных: View порождает Intent "
     "(намерение пользователя), Model преобразует его в новое состояние, View отображает состояние. "
     "Подход удобен для реактивных интерфейсов и сложных состояний, но требует дисциплины "
     "и дополнительной инфраструктуры (RxJava, Kotlin Flow, редьюсеры).")

doc.add_heading("VIPER (View — Interactor — Presenter — Entity — Router)", level=3)
para("VIPER дробит модуль на пять компонентов и хорошо подходит для крупных команд, "
     "где важна максимальная изоляция обязанностей. Плата за это — большое количество классов "
     "и сложность реализации даже для простого экрана; шаблон исторически распространён в iOS.")

para("Сравнение подходов приведено в таблице 1.", first_line=False)
add_table(
    ["Шаблон", "Компоненты", "Когда применять", "Ограничения"],
    [
        ["MVC", "Model, View, Controller", "Простые приложения, веб-разработка", "Контроллер перегружается, View связана с Model"],
        ["MVP", "Model, View, Presenter", "Экраны с большим числом пользовательских действий", "Много интерфейсов, Presenter разрастается"],
        ["MVVM", "Model, View, ViewModel", "Android-приложения с lifecycle и LiveData", "Нужны дисциплина состояний и фабрики зависимостей"],
        ["MVI", "Model, View, Intent", "Реактивные интерфейсы, сложные состояния", "Требует редьюсеров и управления потоками"],
        ["VIPER", "View, Interactor, Presenter, Entity, Router", "Крупные модульные проекты (в основном iOS)", "Много компонентов на каждый модуль"],
    ],
    widths=[2.2, 4.0, 5.2, 5.0],
    font_size=9.5,
)
para("Таблица 1 — Сравнение архитектурных шаблонов", italic=True, size=10, color=GREY,
     align=WD_ALIGN_PARAGRAPH.CENTER, first_line=False, space_after=10)
para("Для Cybersport Tracker выбран MVVM. Причины: проект уже разделён на модули app/domain/data, "
     "использует AndroidX и Java, а главное — должен сохранять состояние при повороте экрана "
     "и реагировать на изменения базы данных. MVVM и LiveData решают эти задачи без написания "
     "собственного жизненного цикла и без привязки ViewModel к Activity.")

doc.add_heading("1.2 MVVM и чистая архитектура проекта", level=2)
para("В проекте реализована схема «чистой архитектуры» с правилом зависимостей, направленных "
     "внутрь: слой представления зависит от domain, слой data реализует интерфейсы domain, "
     "а domain не знает ни об Android, ни о Room, ни о JSON. Такое разделение позволяет заменить "
     "источник данных, не меняя ViewModel и Activity.")
bullet("app (presentation) — Activity, RecyclerView-адаптеры, ViewModel, фабрики ViewModel, "
       "UI-состояния, AppContainer как composition root.")
bullet("domain — доменные модели Team, Match, Tournament, Prediction, User; интерфейсы "
       "TeamRepository, FavoritesRepository, UserRepository и др.; use case-сценарии "
       "(GetTeamsUseCase, AddTeamToFavoritesUseCase, GetTeamByIdUseCase и т. д.).")
bullet("data — DTO и мапперы, NetworkApi с mock-JSON, Room (сущности, DAO, AppDatabase), "
       "SharedPreferences, Firebase Auth, TFLite-источник; реализации репозиториев.")
para("Схема зависимостей и поток данных показаны на рисунке 1.", first_line=False)
add_image("00-architecture.png", width_cm=15.5,
          cap="Рисунок 1 — Архитектура приложения Cybersport Tracker")
para("Ключевая идея: Activity не обращается к репозиторию или NetworkApi напрямую. Она создаёт "
     "ViewModel через ViewModelProvider и фабрику, а ViewModel вызывает use case. Единственное "
     "место, где собираются реализации, — AppContainer; Activity получает из него уже готовые "
     "сценарии и наблюдаемые источники.")

doc.add_heading("1.3 ViewModel: жизненный цикл, ViewModelProvider, Factory", level=2)
para("ViewModel — компонент AndroidX, предназначенный для хранения и управления данными, "
     "связанными со слоем представления. Он привязан к ViewModelStoreOwner (Activity или Fragment) "
     "и сохраняется при пересоздании Activity, вызванном поворотом экрана или сменой конфигурации. "
     "Когда владелец окончательно уничтожается, вызывается protected-метод onCleared(), "
     "в котором освобождаются ресурсы: останавливаются executor-ы, снимаются подписки.")
para("Важно понимать границы: ViewModel не является заменой onSaveInstanceState. Если операционная "
     "система уничтожает процесс приложения, ViewModel тоже теряется; для восстановления "
     "критичных данных нужны SavedStateHandle или постоянное хранилище. В рамках лабораторной "
     "работы ViewModel решает именно проблему пересоздания Activity при повороте.")
para("Экземпляр ViewModel нельзя создавать оператором new в Activity, если требуется правильное "
     "связывание с жизненным циклом. Используется ViewModelProvider:")
add_code("""
vm = new ViewModelProvider(this, new TeamsViewModelFactory(container))
        .get(TeamsViewModel.class);
""", "Листинг 1 — Корректное получение ViewModel")
para("Если у ViewModel есть зависимости, создаётся фабрика, реализующая ViewModelProvider.Factory. "
     "Фабрика изолирует Activity от способа конструирования зависимостей и позволяет передать "
     "в ViewModel use case-сценарии и LiveData-источники:")
add_code("""
public class TeamsViewModelFactory implements ViewModelProvider.Factory {
    private final AppContainer container;

    public TeamsViewModelFactory(AppContainer container) {
        this.container = container;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TeamsViewModel.class)) {
            return (T) new TeamsViewModel(
                    container.teams(),
                    container.addToFavorites(),
                    container.removeFromFavorites(),
                    container.profile(),
                    container.logout(),
                    container.observeFavoriteTeams()
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
""", "Листинг 2 — Фабрика ViewModel")
para("ViewModel не должна хранить ссылки на Activity, View или Context: это приводит к утечкам "
     "памяти. В проекте ViewModel работает только с доменными моделями, use case-сценариями "
     "и LiveData; Android-контекст в неё не передаётся.")

doc.add_heading("1.4 LiveData и организация реактивного состояния", level=2)
para("LiveData — наблюдаемый держатель данных, учитывающий жизненный цикл. Наблюдатель "
     "(Observer) получает обновления только тогда, когда LifecycleOwner находится в активном "
     "состоянии (STARTED или RESUMED). Это автоматически предотвращает обновление уничтоженной "
     "Activity и утечки памяти. При переходе экрана в активное состояние LiveData доставляет "
     "последнее установленное значение — поэтому после поворота интерфейс восстанавливается "
     "без повторной загрузки данных.")
para("Основные операции:")
bullet("setValue() — установка значения из главного потока;")
bullet("postValue() — установка значения из фонового потока (значение доставляется на main thread);")
bullet("observe(owner, observer) — подписка с учётом жизненного цикла;")
bullet("Transformations.map()/switchMap() — преобразование одного LiveData в другое.")
para("В проекте используются два вида LiveData. MutableLiveData применяется внутри ViewModel "
     "для изменения состояния, а наружу отдаётся неизменяемый тип LiveData — Activity не может "
     "случайно изменить состояние в обход ViewModel. Для разовых событий (сообщение Snackbar, "
     "навигация после выхода из аккаунта) реализован SingleLiveEvent, который доставляет значение "
     "только один раз и не повторяет его после поворота экрана.")
add_code("""
public class SingleLiveEvent<T> extends MutableLiveData<T> {
    private final AtomicBoolean pending = new AtomicBoolean(false);

    @Override
    public void setValue(T value) {
        pending.set(true);
        super.setValue(value);
    }

    @Override
    public void postValue(T value) {
        pending.set(true);
        super.postValue(value);
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner,
                        @NonNull Observer<? super T> observer) {
        super.observe(owner, value -> {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(value);
            }
        });
    }
}
""", "Листинг 3 — SingleLiveEvent для разовых событий")

doc.add_heading("1.5 MediatorLiveData как средство объединения источников", level=2)
para("MediatorLiveData — наследник LiveData, который может наблюдать за несколькими источниками "
     "одновременно. Метод addSource(LiveData<S> source, Observer<S> onChanged) подписывается "
     "на источник и вызывает обработчик при каждом его изменении; removeSource() отписывается. "
     "Это позволяет собрать несколько потоков данных в одно согласованное состояние.")
para("Типичный сценарий — экран, которому нужны данные из сети и из локальной базы. Если "
     "обрабатывать их в Activity двумя отдельными observe-колбэками, легко получить рассинхронизацию: "
     "один колбэк уже обновил список, второй ещё нет. MediatorLiveData переносит объединение "
     "в ViewModel: любое изменение любого источника пересчитывает единое состояние TeamsUiState, "
     "и Activity получает уже согласованный снимок.")
para("В Cybersport Tracker MediatorLiveData объединяет:")
bullet("MutableLiveData<List<Team>> с командами из замоканного сетевого слоя;")
bullet("LiveData<List<Team>> из Room с избранными командами;")
bullet("MutableLiveData<Boolean> loading, MutableLiveData<String> query и country — служебные "
       "состояния загрузки и фильтрации.")
para("Схема объединения представлена на рисунке 2.", first_line=False)
add_image("00-mediator.png", width_cm=15.5,
          cap="Рисунок 2 — MediatorLiveData: два источника и одно состояние экрана")
para("Когда пользователь нажимает на «сердечко», ViewModel в фоновом executor-е вызывает "
     "AddTeamToFavoritesUseCase. Репозиторий пишет строку в Room. Room сам обновляет LiveData, "
     "MediatorLiveData пересчитывает TeamsUiState, Activity получает новый список и передаёт его "
     "в адаптер. Ручной вызов notifyDataSetChanged() не требуется.")

doc.add_heading("1.6 RecyclerView: Adapter, ViewHolder, LayoutManager, DiffUtil", level=2)
para("RecyclerView — компонент для отображения больших наборов данных в виде списка или сетки. "
     "В отличие от ListView, он не создаёт View для каждого элемента, а переиспользует уже "
     "созданные представления, вышедшие за пределы экрана (view recycling). Это снижает "
     "потребление памяти и делает прокрутку плавной.")
para("Основные участники:")
bullet("Adapter — посредник между данными и RecyclerView; создаёт ViewHolder и привязывает данные;")
bullet("ViewHolder — хранит ссылки на View одного элемента, чтобы не вызывать findViewById() "
       "при каждой прокрутке;")
bullet("LayoutManager — отвечает за расположение элементов (LinearLayoutManager, GridLayoutManager, "
       "StaggeredGridLayoutManager);")
bullet("ItemDecoration — разделители и отступы; ItemAnimator — анимации изменений; "
       "ItemTouchHelper — drag-and-drop и swipe-to-dismiss.")
para("Adapter обязан переопределить три метода: onCreateViewHolder() (создание ViewHolder "
     "из XML-разметки), onBindViewHolder() (привязка данных к позиции) и getItemCount() "
     "(количество элементов). В проекте используется ListAdapter — наследник RecyclerView.Adapter, "
     "который сам применяет DiffUtil. DiffUtil сравнивает старый и новый списки и сообщает "
     "RecyclerView, какие элементы добавлены, удалены или изменены; анимаются только реальные "
     "изменения, а не весь список.")
para("Клик по элементу обрабатывается через слушатель, переданный в адаптер; клик по кнопке "
     "избранного — отдельный слушатель. Это позволяет Activity оставаться координатором "
     "навигации, не «зашивая» её в адаптер.")

# ---------------------------------------------------------------- lab 3
doc.add_heading("2 Практическая работа № 3. ViewModel, LiveData, MediatorLiveData", level=1)
doc.add_heading("2.1 Контрольное задание и исходное состояние проекта", level=2)
para("Контрольное задание практической работы № 3 содержит три требования:", first_line=False)
numbered_reset()
numbered("взаимодействие Activity со слоем domain должно осуществляться через ViewModel;")
numbered("обновление состояния интерфейса должно осуществляться через LiveData;")
numbered("необходимо самостоятельно изучить MediatorLiveData и получить данные из замоканных "
         "данных сетевой части и базы данных.")
para("Исходная версия приложения этим требованиям не удовлетворяла. MainActivity напрямую "
     "создавала AppContainer и вызывала container.profile(), container.teams(), "
     "container.addToFavorites() и другие сценарии, а результат выводила в один TextView. "
     "Такая реализация имела несколько недостатков:")
bullet("бизнес-логика и навигация были сосредоточены в Activity (более 200 строк);")
bullet("при повороте экрана Activity пересоздавалась и все сценарии выполнялись заново;")
bullet("не было единого состояния экрана и реактивного обновления при изменении базы;")
bullet("код было сложно тестировать без Android-инструментов.")
para("В ходе работы презентационный слой был переработан: появились LoginViewModel, "
     "TeamsViewModel и TeamDetailsViewModel, состояния LoginUiState, TeamsUiState, "
     "TeamDetailsUiState, фабрики ViewModel и реактивные источники данных.")

doc.add_heading("2.2 Экранные ViewModel и фабрики", level=2)
para("LoginViewModel инкапсулирует вход и регистрацию. Activity передаёт в него введённые "
     "e-mail и пароль, а получает LoginUiState с флагами loading, error и user. Проверка пустых "
     "полей и обработка ошибок также перенесены во ViewModel; Activity лишь преобразует код "
     "ошибки в строковый ресурс.")
para("TeamsViewModel — центральный класс экрана «Команды». Он получает из AppContainer "
     "use case-сценарии, профиль пользователя и LiveData избранного. Внутри создаётся "
     "single-thread executor для операций с Room и mock-сетевым слоем, чтобы не блокировать "
     "главный поток. Наружу ViewModel отдаёт два LiveData: state (единое состояние экрана) "
     "и message (разовое сообщение).")
add_code("""
public class TeamsViewModel extends ViewModel {
    private final GetTeamsUseCase getTeamsUseCase;
    private final AddTeamToFavoritesUseCase addTeamToFavoritesUseCase;
    private final RemoveTeamFromFavoritesUseCase removeTeamFromFavoritesUseCase;
    private final LogoutUseCase logoutUseCase;
    private final User user;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<Team>> networkTeams =
            new MutableLiveData<>(Collections.emptyList());
    private final LiveData<List<Team>> favoriteTeams;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    private final MutableLiveData<String> query = new MutableLiveData<>("");
    private final MutableLiveData<String> country = new MutableLiveData<>("");
    private final MediatorLiveData<TeamsUiState> state = new MediatorLiveData<>();
    private final SingleLiveEvent<Integer> message = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> loggedOut = new SingleLiveEvent<>();

    private volatile Set<Integer> favoriteIds = Collections.emptySet();
}
""", "Листинг 4 — Поля TeamsViewModel")
para("Загрузка команд выполняется в фоне, а результат публикуется через postValue(). "
     "Это принципиально: Room и «сетевой» слой не должны работать в главном потоке.")
add_code("""
private void loadTeams() {
    executor.execute(() -> {
        List<Team> loaded = getTeamsUseCase.execute();
        networkTeams.postValue(loaded == null
                ? Collections.emptyList()
                : new ArrayList<>(loaded));
        loading.postValue(false);
    });
}
""", "Листинг 5 — Асинхронная загрузка данных")
para("TeamDetailsViewModel устроен аналогично, но дополнительно загружает матчи команды "
     "и фильтрует их по идентификатору. Его состояние содержит команду, список матчей, "
     "признак избранного и признак авторизации. Благодаря этому Activity не выполняет "
     "ни одного запроса к domain-слою.")

doc.add_heading("2.3 Обновление интерфейса через LiveData", level=2)
para("Activity подписывается на LiveData в onCreate() и передаёт состояние в метод render(). "
     "render() не содержит бизнес-логики: он только показывает или скрывает блоки, заполняет "
     "текстовые поля и передаёт список в адаптер. После поворота экрана Activity создаётся заново, "
     "но ViewModel остаётся той же, и LiveData немедленно отдаёт последнее состояние.")
add_code("""
viewModel.getState().observe(this, this::render);
viewModel.getMessage().observe(this, messageRes ->
        Snackbar.make(root, messageRes, Snackbar.LENGTH_LONG).show());
viewModel.getLoggedOut().observe(this, loggedOut -> {
    if (Boolean.TRUE.equals(loggedOut)) {
        openLogin();
    }
});
""", "Листинг 6 — Подписка Activity на LiveData")
para("Метод render() реализует декларативное отображение состояния:")
add_code("""
private void render(TeamsUiState state) {
    currentUser = state.getUser();
    adapter.submitList(state.getTeams());

    boolean showList = !state.isLoading() && !state.isEmpty();
    recyclerView.setVisibility(showList ? View.VISIBLE : View.INVISIBLE);
    loadingView.setVisibility(state.isLoading() ? View.VISIBLE : View.GONE);
    emptyView.setVisibility(state.isEmpty() ? View.VISIBLE : View.GONE);
    guestCard.setVisibility(state.isAuthorized() ? View.GONE : View.VISIBLE);

    if (state.isLoading()) {
        countView.setText(R.string.teams_loading);
    } else if (state.hasFilter()) {
        countView.setText(getString(R.string.teams_found, state.getFoundCount()));
    } else {
        countView.setText(getString(R.string.teams_total, state.getTotalCount()));
    }
}
""", "Листинг 7 — Отрисовка состояния экрана")
para("Такой подход устраняет ручные вызовы setText() в обработчиках нажатий и гарантирует, "
     "что все элементы экрана отображают одно и то же состояние. Например, при поиске "
     "одновременно обновляются счётчик, список и пустое состояние.")

doc.add_heading("2.4 Объединение сетевых данных и Room с помощью MediatorLiveData", level=2)
para("Для выполнения третьего пункта задания в DAO избранного добавлен метод, возвращающий "
     "LiveData. Room поддерживает реактивные запросы: после вставки или удаления строки "
     "он сам пересчитывает результат и уведомляет наблюдателей.")
add_code("""
@Query("SELECT * FROM favorite_teams ORDER BY worldRank ASC")
LiveData<List<FavoriteTeamEntity>> observeAll();
""", "Листинг 8 — Реактивный запрос Room")
para("Репозиторий преобразует сущности в доменные модели с помощью Transformations.map, "
     "не раскрывая слой data наружу:")
add_code("""
public LiveData<List<Team>> observeFavoriteTeams() {
    return Transformations.map(favoriteTeamDao.observeAll(),
            FavoriteTeamEntityMapper::toDomainList);
}
""", "Листинг 9 — Репозиторий избранного")
para("В конструкторе TeamsViewModel MediatorLiveData получает пять источников. Обработчики "
     "не пытаются обновить UI напрямую — каждый из них лишь вызывает emitState(), который "
     "пересчитывает единое состояние.")
add_code("""
state.addSource(networkTeams, teams -> emitState());
state.addSource(loading, isLoading -> emitState());
state.addSource(query, value -> emitState());
state.addSource(country, value -> emitState());
state.addSource(favoriteTeams, teams -> {
    Set<Integer> ids = new HashSet<>();
    if (teams != null) {
        for (Team team : teams) {
            ids.add(team.getId());
        }
    }
    favoriteIds = ids;
    emitState();
});
""", "Листинг 10 — Подключение источников к MediatorLiveData")
para("Метод emitState() применяет поиск и фильтр, добавляет признак избранного и формирует "
     "неизменяемый TeamsUiState:")
add_code("""
List<TeamListItem> visible = new ArrayList<>();
for (Team team : all) {
    if (!matchesCountry(team, currentCountry)) {
        continue;
    }
    if (!matchesQuery(team, currentQuery)) {
        continue;
    }
    visible.add(new TeamListItem(
            team,
            TeamLogoResolver.resolve(team),
            user != null && favoriteIds.contains(team.getId()),
            user != null
    ));
}
state.setValue(new TeamsUiState(
        isLoading, user, visible, all.size(), visible.size(),
        currentQuery, currentCountry, !isLoading && visible.isEmpty()
));
""", "Листинг 11 — Формирование единого состояния")
para("Переключение избранного также выполняется в фоновом потоке. После записи в Room "
     "срабатывает observeAll(), MediatorLiveData пересчитывает состояние, и сердечко "
     "меняет цвет без перезагрузки экрана и без вызова notifyDataSetChanged():")
add_code("""
public void toggleFavorite(Team team) {
    if (team == null) {
        return;
    }
    if (user == null) {
        message.setValue(R.string.teams_login_hint);
        return;
    }
    executor.execute(() -> {
        if (favoriteIds.contains(team.getId())) {
            removeTeamFromFavoritesUseCase.execute(team.getId());
        } else {
            addTeamToFavoritesUseCase.execute(team);
        }
    });
}
""", "Листинг 12 — Переключение избранного")
para("Аналогичный MediatorLiveData используется в TeamDetailsViewModel: он объединяет команду, "
     "её матчи, флаг загрузки и наблюдаемое избранное. Это демонстрирует, что подход "
     "масштабируется на несколько экранов, а не является единичным трюком.")

doc.add_heading("2.5 Результаты практической работы № 3", level=2)
para("После рефакторинга Activity перестала обращаться к use case-сценариям напрямую. "
     "Взаимодействие с domain-слоем идёт только через ViewModel, состояние интерфейса "
     "обновляется через LiveData, а MediatorLiveData объединяет mock-сеть и Room. "
     "На рисунках 3–6 показаны основные экраны приложения.")
add_image("01-login.png", width_cm=8.4,
          cap="Рисунок 3 — Экран авторизации: тёмная тема, поля Material 3")
add_image("02-teams.png", width_cm=8.4,
          cap="Рисунок 4 — Экран «Команды» после входа: данные из mock-слоя")
add_image("07-guest-teams.png", width_cm=8.4,
          cap="Рисунок 5 — Гостевой режим: подсказка и неактивные действия избранного")
add_image("08-guest-details.png", width_cm=8.4,
          cap="Рисунок 6 — Карточка команды в гостевом режиме: заблокированная кнопка")

# ---------------------------------------------------------------- lab 4
doc.add_heading("3 Практическая работа № 4. RecyclerView и передача данных через LiveData", level=1)
doc.add_heading("3.1 Контрольное задание", level=2)
para("Контрольное задание практической работы № 4 требует открыть приложение из практического "
     "занятия № 1 и изменить его по аналогии с примером: создать в репозитории заглушку "
     "с набором данных, которые планируется получать от внешнего сервиса, передать эти данные "
     "в слой представления с помощью LiveData и установить в RecyclerView.")
para("В проекте Cybersport Tracker такая заглушка уже существовала в виде NetworkApi, "
     "возвращающего JSON-строки. В ходе работы она была дополнена и явно отделена от "
     "реализации: DTO → маппер → доменная модель → use case → ViewModel → LiveData → RecyclerView. "
     "Это позволяет позже заменить NetworkApi на Retrofit, не меняя UI.")

doc.add_heading("3.2 Заглушка набора данных в слое data", level=2)
para("NetworkApi содержит константы TEAMS_JSON, TOURNAMENTS_JSON, MATCHES_JSON и USERS_JSON. "
     "Gson разбирает их в DTO, а TeamMapper преобразует TeamDto в доменную модель Team. "
     "Пять команд соответствуют реалистичному контенту из технического задания:")
add_code("""
private static final String TEAMS_JSON = "["
        + "{\\"id\\":1,\\"tag\\":\\"NAVI\\",\\"name\\":\\"Natus Vincere\\","
        + "\\"country\\":\\"Украина\\",\\"logo_url\\":\\"https://mockapi.io/logos/navi.png\\","
        + "\\"world_rank\\":1,\\"players\\":[\\"s1mple\\",\\"b1t\\",\\"jL\\",\\"Aleksib\\",\\"iM\\"]},"
        + "{\\"id\\":2,\\"tag\\":\\"Spirit\\",\\"name\\":\\"Team Spirit\\","
        + "\\"country\\":\\"Россия\\",\\"world_rank\\":2, ...}"
        + "]";
""", "Листинг 13 — Фрагмент заглушки сетевого ответа")
para("TeamRepositoryImpl реализует интерфейс domain-слоя и возвращает List<Team>. "
     "GetTeamsUseCase инкапсулирует вызов репозитория, а ViewModel загружает данные "
     "в фоновом потоке и публикует их в MutableLiveData. Таким образом, источник данных "
     "остаётся заменяемым: Activity и адаптер не знают, откуда пришли команды.")

doc.add_heading("3.3 Реализация списка команд на RecyclerView", level=2)
para("Разметка экрана содержит RecyclerView, который занимает всё доступное пространство "
     "между панелью поиска и нижней границей. Элемент списка — MaterialCardView с ConstraintLayout "
     "внутри: логотип 48 dp в скруглённом контейнере, тег команды, полное название, страна "
     "и место в рейтинге, а также кнопка избранного 48 dp. Размеры и отступы соответствуют "
     "8-pt системе, радиус карточки — 14 dp, цвет поверхности — #161B22.")
add_code("""
<com.google.android.material.card.MaterialCardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardBackgroundColor="@color/surface"
    app:cardCornerRadius="14dp"
    app:cardElevation="0dp"
    app:strokeColor="@color/border"
    app:strokeWidth="1dp">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:padding="16dp">

        <ImageView android:id="@+id/imageTeamLogo"
            android:layout_width="48dp"
            android:layout_height="48dp"
            android:background="@drawable/bg_logo_tile"
            android:padding="4dp" />
        <!-- tag, name, meta, favorite -->
    </androidx.constraintlayout.widget.ConstraintLayout>
</com.google.android.material.card.MaterialCardView>
""", "Листинг 14 — Фрагмент разметки элемента списка")
para("TeamsAdapter наследуется от ListAdapter и использует DiffUtil. Внутренний TeamViewHolder "
     "хранит ссылки на ImageView, три TextView и ImageButton; метод bind() заполняет их "
     "и навешивает обработчики кликов. Логотип выбирается через TeamLogoResolver по "
     "идентификатору команды: в офлайн-демонстрации используются локальные PNG-ресурсы, "
     "а в production здесь была бы загрузка по logoUrl.")
add_code("""
public class TeamsAdapter extends ListAdapter<TeamListItem, TeamsAdapter.TeamViewHolder> {

    private static final DiffUtil.ItemCallback<TeamListItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TeamListItem>() {
                @Override
                public boolean areItemsTheSame(TeamListItem oldItem, TeamListItem newItem) {
                    return oldItem.getTeam().getId() == newItem.getTeam().getId();
                }

                @Override
                public boolean areContentsTheSame(TeamListItem oldItem, TeamListItem newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public TeamsAdapter(Listener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team, parent, false);
        return new TeamViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
""", "Листинг 15 — Адаптер списка команд")
para("Каждый элемент получает два обработчика: onTeamClick() открывает TeamDetailsActivity "
     "с идентификатором команды, onFavoriteClick() передаёт команду в ViewModel. "
     "DiffUtil сравнивает элементы по id и содержимому, поэтому после обновления избранного "
     "перерисовывается только изменённая карточка.")

doc.add_heading("3.4 Передача данных из ViewModel в адаптер", level=2)
para("Цепочка передачи данных выглядит так:", first_line=False)
numbered_reset()
numbered("NetworkApi возвращает mock-JSON;")
numbered("TeamRepositoryImpl и GetTeamsUseCase преобразуют его в List<Team>;")
numbered("TeamsViewModel загружает список в executor и публикует в MutableLiveData;")
numbered("MediatorLiveData объединяет команды с избранным и формирует List<TeamListItem>;")
numbered("Activity получает TeamsUiState через observe();")
numbered("adapter.submitList(state.getTeams()) передаёт список в ListAdapter;")
numbered("DiffUtil вычисляет различия, RecyclerView перерисовывает только изменившиеся элементы.")
para("Адаптер не подписывается на LiveData самостоятельно: это нарушило бы жизненный цикл "
     "и усложнило бы повторное использование. Activity является владельцем жизненного цикла "
     "и передаёт в адаптер уже готовый неизменяемый список. Такой подход соответствует "
     "рекомендациям Android и упрощает тестирование.")

doc.add_heading("3.5 Состояния списка и результаты работы", level=2)
para("Экран поддерживает несколько состояний, которые переключаются через TeamsUiState:")
bullet("загрузка — CircularProgressIndicator и подпись «Загрузка команд…»;")
bullet("список — карточки команд с логотипами и избранным;")
bullet("пустой результат — иконка, заголовок «Команды не найдены» и подсказка изменить запрос;")
bullet("гостевой режим — карточка-подсказка «Войдите, чтобы сохранять команды» и приглушённые "
       "кнопки избранного;")
bullet("поиск и фильтрация по стране — счётчик «Найдено команд: N» и отфильтрованный список.")
para("На рисунках 7–10 показаны состояния списка и карточка команды.", first_line=False)
add_image("03-favorite.png", width_cm=8.4,
          cap="Рисунок 7 — Избранное: сердечко NAVI обновилось через Room LiveData")
add_image("04-empty-search.png", width_cm=8.4,
          cap="Рисунок 8 — Пустое состояние поиска")
add_image("05-team-details.png", width_cm=8.4,
          cap="Рисунок 9 — Карточка команды: состав и матчи")
add_image("06-team-details-info.png", width_cm=8.4,
          cap="Рисунок 10 — Нижняя часть карточки: информация и кнопка избранного")

# ---------------------------------------------------------------- testing
doc.add_page_break()
doc.add_heading("4 Тестирование и проверка работоспособности", level=1)
para("Проверка выполнялась на эмуляторе Medium_Phone_API_36.1 (Android 16, 1080×2400). "
     "Проект собирался командой assembleDebug; APK устанавливался через adb. "
     "Результаты сценариев приведены в таблице 2.", first_line=False)
add_table(
    ["№", "Сценарий", "Ожидаемый результат", "Факт"],
    [
        ["1", "Сборка проекта (assembleDebug)", "BUILD SUCCESSFUL, APK создан", "Выполнено"],
        ["2", "Вход admin@test.ru / 123456", "Переход на экран «Команды»", "Выполнено"],
        ["3", "Загрузка списка", "5 команд с логотипами и рейтингом", "Выполнено"],
        ["4", "Нажатие на сердечко NAVI", "Запись в Room, сердечко синее без перезагрузки", "Выполнено"],
        ["5", "Поиск «zzz»", "Счётчик 0, пустое состояние", "Выполнено"],
        ["6", "Фильтр «Россия»", "Одна команда Team Spirit", "Выполнено"],
        ["7", "Открытие карточки команды", "Состав, матчи, информация, кнопка избранного", "Выполнено"],
        ["8", "Выход и «Продолжить без входа»", "Гостевая подсказка, заблокированное избранное", "Выполнено"],
        ["9", "Поворот экрана", "Фильтр и состояние сохраняются благодаря ViewModel", "Выполнено"],
    ],
    widths=[1.0, 5.8, 7.0, 2.7],
    font_size=9.5,
)
para("Таблица 2 — Результаты проверки сценариев", italic=True, size=10, color=GREY,
     align=WD_ALIGN_PARAGRAPH.CENTER, first_line=False, space_after=10)
para("Отдельно проверено, что после поворота экрана выбранный фильтр «Россия» и гостевой режим "
     "сохраняются: Activity создаётся заново, но ViewModel остаётся той же, а LiveData "
     "немедленно отдаёт последнее состояние. Это подтверждает корректность выбранной архитектуры.")
para("Ограничения учебной реализации: сетевой слой является заглушкой и не выполняет реальных "
     "HTTP-запросов; логотипы команд локальные; авторизация использует mock-провайдер при "
     "недоступности Firebase. Эти ограничения не влияют на демонстрацию архитектурных решений "
     "и могут быть устранены заменой реализаций в слое data.")

# ---------------------------------------------------------------- conclusion
doc.add_heading("Заключение", level=1)
para("В ходе выполнения практических работ № 3 и № 4 приложение Cybersport Tracker было "
     "переведено на архитектуру MVVM. Activity больше не содержит бизнес-логики и не обращается "
     "к репозиториям напрямую: взаимодействие со слоем domain инкапсулировано в LoginViewModel, "
     "TeamsViewModel и TeamDetailsViewModel, а состояние интерфейса передаётся через LiveData.")
para("Освоен MediatorLiveData: на экране «Команды» он объединяет замоканные данные сетевого "
     "слоя и реактивный запрос Room к избранному. Любое изменение базы автоматически "
     "пересчитывает единое состояние, поэтому интерфейс обновляется без ручных уведомлений "
     "адаптера. Это устраняет рассинхронизацию между списком, счётчиком и кнопками.")
para("Для отображения списка команд реализован RecyclerView с ListAdapter и DiffUtil: "
     "ViewHolder переиспользуются, изображения логотипов выводятся через ImageView, "
     "а изменения списка анимируются точечно. Передача данных выстроена в цепочку "
     "«заглушка → репозиторий → use case → ViewModel → LiveData → Activity → адаптер».")
para("Полученный код собирается, устанавливается на эмулятор и проходит проверку основных "
     "сценариев: авторизация, гостевой режим, избранное, поиск, фильтрация, карточка команды "
     "и поворот экрана. Архитектура готова к дальнейшему развитию: замене mock-API на Retrofit, "
     "добавлению новых экранов и модульному тестированию ViewModel.")

# ---------------------------------------------------------------- sources
doc.add_heading("Список использованных источников", level=1)
sources = [
    "Android Developers. ViewModel overview. — URL: https://developer.android.com/topic/libraries/architecture/viewmodel (дата обращения: 02.11.2026).",
    "Android Developers. LiveData overview. — URL: https://developer.android.com/topic/libraries/architecture/livedata (дата обращения: 02.11.2026).",
    "Android Developers. MediatorLiveData. — URL: https://developer.android.com/reference/androidx/lifecycle/MediatorLiveData (дата обращения: 02.11.2026).",
    "Android Developers. RecyclerView. — URL: https://developer.android.com/develop/ui/views/layout/recyclerview (дата обращения: 02.11.2026).",
    "Android Developers. Save data in a local database using Room. — URL: https://developer.android.com/training/data-storage/room (дата обращения: 02.11.2026).",
    "Android Developers. Guide to app architecture. — URL: https://developer.android.com/topic/architecture (дата обращения: 02.11.2026).",
    "Material Design 3. Components. — URL: https://m3.material.io/components (дата обращения: 02.11.2026).",
    "Gradle. Android plugin for Gradle. — URL: https://developer.android.com/build (дата обращения: 02.11.2026).",
]
for i, source in enumerate(sources, 1):
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.LEFT
    p.paragraph_format.left_indent = Cm(0.8)
    p.paragraph_format.first_line_indent = Cm(-0.8)
    p.paragraph_format.line_spacing = 1.15
    p.paragraph_format.space_after = Pt(4)
    p.add_run(f"{i}. {source}").font.size = Pt(11)

# ---------------------------------------------------------------- appendix
doc.add_heading("Приложение А. Перечень изменённых файлов", level=1)
para("В таблице А.1 перечислены основные файлы, созданные или изменённые в ходе работ.", first_line=False)
add_table(
    ["Модуль", "Файл", "Назначение"],
    [
        ["gradle", "libs.versions.toml, app/build.gradle.kts, data/build.gradle.kts",
         "Зависимости lifecycle, RecyclerView, CardView"],
        ["domain", "RemoveTeamFromFavoritesUseCase.java, LogoutUseCase.java",
         "Новые сценарии использования"],
        ["data", "FavoriteTeamDao.java, FavoritesRepositoryImpl.java",
         "Реактивный запрос Room и LiveData избранного"],
        ["data", "NetworkApi.java, CompositeAuthDataSource.java",
         "Заглушка данных и устойчивая авторизация"],
        ["app", "TeamsViewModel.java, TeamsViewModelFactory.java, TeamsUiState.java, TeamListItem.java",
         "Состояние списка, MediatorLiveData, фильтрация"],
        ["app", "TeamDetailsViewModel.java, TeamDetailsViewModelFactory.java, TeamDetailsUiState.java",
         "Состояние карточки команды"],
        ["app", "LoginViewModel.java, LoginViewModelFactory.java, LoginUiState.java",
         "Состояние авторизации"],
        ["app", "TeamsAdapter.java, TeamLogoResolver.java, SingleLiveEvent.java",
         "RecyclerView-адаптер, локальные логотипы, разовые события"],
        ["app", "MainActivity.java, LoginActivity.java, TeamDetailsActivity.java",
         "Экраны и подписка на LiveData"],
        ["app", "activity_main.xml, activity_login.xml, activity_team_details.xml, item_team.xml, item_player.xml",
         "Разметка в стиле тёмного broadcast-интерфейса"],
        ["app", "colors.xml, themes.xml, dimens.xml, drawable/*",
         "Палитра, типографика, иконки, логотипы"],
    ],
    widths=[2.0, 7.2, 8.0],
    font_size=9.5,
)
para("Таблица А.1 — Основные изменения в проекте", italic=True, size=10, color=GREY,
     align=WD_ALIGN_PARAGRAPH.CENTER, first_line=False, space_after=10)

doc.save(OUT)
print("saved:", OUT)
