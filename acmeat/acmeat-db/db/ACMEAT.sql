-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Creato il: Lug 21, 2025 alle 17:54
-- Versione del server: 10.4.32-MariaDB
-- Versione PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ACMEAT`
--
CREATE DATABASE IF NOT EXISTS `ACMEAT` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `ACMEAT`;

-- --------------------------------------------------------

--
-- Struttura della tabella `LOCALE`
--

CREATE TABLE `LOCALE` (
  `ID` int(11) NOT NULL,
  `NOME` varchar(50) NOT NULL,
  `ORA_APERTURA` varchar(50) NOT NULL,
  `ORA_CHIUSURA` varchar(50) NOT NULL,
  `INDIRIZZO` varchar(100) NOT NULL,
  `GIORNI_APERTURA` varchar(100) NOT NULL,
  `DISPONIBILE` tinyint(1) NOT NULL,
  `URL` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `LOCALE`
--

INSERT INTO `LOCALE` (`ID`, `NOME`, `ORA_APERTURA`, `ORA_CHIUSURA`, `INDIRIZZO`, `GIORNI_APERTURA`, `DISPONIBILE`, `URL`) VALUES
(1, 'Braci e Basilico', '09:00', '18:00', 'VIa Aldrovanni 16, Bologna', 'Lun,\r\nMar,\r\nMer,\r\nGio,\r\nVen,\r\nDom', 1, 'braciebasilico.romanellas.cloud'),
(1958374512, 'Osteria mare e bosco', '12:00', '22:00', 'Via Guglielmo Oberdan 16, Bologna', 'Lun,Mar,Mer,Gio,Ven', 1, 'osteriamareebosco.romanellas.cloud'),
(1958374513, 'Il Vicoletto Segreto', '11:00', '22:00', 'Via Edoardo Ferravilla 3, Bologna', 'Lun', 1, 'ilvicolettosegreto.romanellas.cloud'),
(1958374514, 'La Forchetta ribelle', '12:00', '22:00', 'Via Mondo 23, Bologna', 'Lun,Mar,Mer,Gio,Ven,Sab', 1, 'laforchettaribelle.romanellas.cloud'),
(1958374515, 'Cantina Fior di sale', '12:00', '22:00', 'Contrada Vazzieri 12, Campobasso', 'Lun,Mar,Mer,Gio', 1, 'cantinafiordisale.romanellas.cloud');

-- --------------------------------------------------------

--
-- Struttura della tabella `MENU`
--

CREATE TABLE `MENU` (
  `ID` int(11) NOT NULL,
  `DESCRIZIONE` varchar(500) NOT NULL,
  `TIPO` varchar(50) NOT NULL,
  `PREZZO` int(11) NOT NULL,
  `ID_LOCALE` int(11) NOT NULL,
  `DATA` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `MENU`
--

INSERT INTO `MENU` (`ID`, `DESCRIZIONE`, `TIPO`, `PREZZO`, `ID_LOCALE`, `DATA`) VALUES
(1, 'nuovo menu', 'Pesce', 0, 1, '2025-06-13'),
(23, 'Prova 1', 'Cena', 23, 23, ''),
(123, 'Daily Menu-braciebasilico.romanellas.cloud-07/06/2025 16:55:44', 'Carne', 24, 1, '2025-07-06'),
(124, 'Daily Menu-braciebasilico.romanellas.cloud-07/16/2025 12:16:29', 'Carne', 5, 1, '2025-07-16'),
(12312, 'Daily Menu-braciebasilico.romanellas.cloud-07/16/2025 12:27:11', 'Carne', 8, 1, '2025-07-16'),
(33333, 'Daily Menu-braciebasilico.romanellas.cloud-07/16/2025 14:08:27', 'Carne', 8, 1, '2025-07-16'),
(129391, 'Daily Menu-cantinafiordisale.romanellas.cloud-07/19/2025 14:52:55', 'Carne', 7, 1958374515, '2025-07-19'),
(1212344, 'Daily Menu-braciebasilico.romanellas.cloud-07/16/2025 13:29:46', 'Carne', 8, 1, '2025-07-16'),
(1232400, 'Daily Menu-cantinafiordisale.romanellas.cloud-07/18/2025 13:13:36', 'Carne', 5, 1958374515, '2025-07-18'),
(12241241, 'Daily Menu-braciebasilico.romanellas.cloud-07/16/2025 12:19:50', 'Carne', 8, 1, '2025-07-16'),
(116724299, 'Daily Menu', 'Carne', 0, 1, '2025-06-13'),
(121244444, 'Daily Menu-braciebasilico.romanellas.cloud-07/19/2025 14:22:32', 'Carne', 8, 1, '2025-07-19'),
(559034578, 'è buono', 'Cena', 50, 0, ''),
(713227581, 'Daily Menu', 'Carne', 0, 1, '2025-06-13'),
(1384498373, 'Daily Menu', 'Carne', 0, 1, '2025-06-13'),
(1443431554, 'Daily Menu', 'Carne', 0, 1, '2025-06-13'),
(1645620109, 'Daily Menu', 'Carne', 0, 1, '2025-06-13'),
(1929440470, 'Daily Menu', 'Carne', 0, 1, '2025-06-13'),
(1966904463, 'Daily Menu', 'Carne', 0, 1, '2025-06-13'),
(1966904464, 'Daily Menu-braciebasilico.romanellas.cloud-07/06/2025 16:50:53', 'Carne', 12, 1, '2025-07-06');

-- --------------------------------------------------------

--
-- Struttura della tabella `ORDINE`
--

CREATE TABLE `ORDINE` (
  `ID_UTENTE` int(11) NOT NULL,
  `ID` int(11) NOT NULL,
  `ID_LOCALE` int(11) NOT NULL,
  `ID_SOC_C` int(11) NOT NULL,
  `ORA_CONSEGNA` varchar(50) NOT NULL,
  `ORA_ACQUISTO` varchar(50) NOT NULL,
  `ID_TRANSAZIONE` varchar(256) NOT NULL,
  `PREZZO` int(11) NOT NULL,
  `ID_MENU` int(11) NOT NULL,
  `QUANTITÀ` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `ORDINE`
--

INSERT INTO `ORDINE` (`ID_UTENTE`, `ID`, `ID_LOCALE`, `ID_SOC_C`, `ORA_CONSEGNA`, `ORA_ACQUISTO`, `ID_TRANSAZIONE`, `PREZZO`, `ID_MENU`, `QUANTITÀ`) VALUES
(1, 13889461, 1958374515, 0, '2025-07-19 18:50', '', '', 20, 129391, 2),
(1, 95201047, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 113705789, 1958374515, 0, '2025-07-19 19:45', '2025-07-19 16:02', 'fa7385f4451b413b835a9962abe98f1629f12abf05efd990ff644b985b080f5c88782c94fa3739307e60c438deef878a', 20, 129391, 2),
(1, 154342669, 1958374515, 0, '2025-07-19 17:24', '2025-07-19 17:21', 'fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1', 20, 129391, 2),
(1, 218016705, 1958374515, 0, '2025-07-19 16:34', '', '', 20, 129391, 2),
(1, 303703816, 1958374515, 0, '2025-07-19 19:45', '', '', 20, 129391, 2),
(1, 316633791, 1958374515, 0, '2025-07-18 17:45', '', '', 20, 1232400, 1),
(1, 387993262, 1958374515, 0, '2025-07-19 17:55', '', '', 20, 129391, 2),
(1, 436251949, 1958374515, 0, '2025-07-19 17:55', '', '', 20, 129391, 2),
(1, 505975975, 1958374515, 0, '2025-07-19 19:20', '2025-07-19 19:17', 'fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1', 20, 129391, 2),
(1, 537346516, 1958374515, 0, '2025-07-18 21:45', '2025-07-18 22:29', 'fa7385f4451b413b835a9962abe98f1629f12abf05efd990ff644b985b080f5c88782c94fa3739307e60c438deef878a', 20, 1232400, 1),
(1, 545662712, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 570131012, 1958374515, 0, '2025-07-18 17:45', '', '', 20, 1232400, 1),
(1, 596482243, 1, 0, '2025-07-19 19:45', '', '', 20, 121244444, 2),
(1, 601826702, 1, 0, '2025-07-19 19:45', '', '', 20, 121244444, 2),
(1, 603659664, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 629933803, 1958374515, 0, '2025-07-19 16:55', '2025-07-19 16:51', 'fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1', 20, 129391, 2),
(1, 654949706, 1958374515, 0, '2025-07-18 21:45', '', '', 20, 1232400, 1),
(1, 657176238, 1958374515, 0, '2025-07-19 19:45', '', '', 20, 129391, 2),
(1, 673956025, 1958374515, 0, '2025-07-19 17:08', '2025-07-19 17:05', 'fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1', 20, 129391, 2),
(1, 686569366, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 730217062, 1958374515, 0, '2025-07-18 17:45', '', '', 20, 1232400, 1),
(1, 730377359, 1958374515, 0, '2025-07-18 21:45', '', '', 20, 1232400, 1),
(1, 798015716, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 810745372, 1958374515, 0, '2025-07-19 19:30', '2025-07-19 19:26', 'fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1', 20, 129391, 2),
(1, 835355918, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 845335900, 1958374515, 0, '2025-07-18 21:45', '', '', 20, 1232400, 1),
(1, 894765156, 1958374515, 0, '2025-07-19 17:55', '', '', 20, 129391, 2),
(1, 915561216, 1958374515, 0, '2025-07-18 17:45', '', '', 20, 1232400, 1),
(1, 931253295, 1958374515, 0, '2025-07-20 19:45', '', '', 20, 129391, 1),
(1, 969783344, 1958374515, 0, '2025-07-18 17:45', '', '', 20, 1232400, 1),
(1, 1023060551, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 1032656657, 1958374515, 0, '2025-07-19 19:24', '2025-07-19 19:22', 'fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1', 20, 129391, 2),
(1, 1069601740, 1958374515, 0, '2025-07-19 17:45', '2025-07-19 19:43', 'fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1', 20, 129391, 2),
(1, 1090580190, 1958374515, 0, '2025-07-19 18:48', '', '', 20, 129391, 2),
(1, 1106370410, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 1115589390, 1958374515, 0, '2025-07-19 19:34', '2025-07-19 19:31', 'fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1', 20, 129391, 2),
(1, 1128888166, 1958374515, 0, '2025-07-19 17:55', '', '', 20, 129391, 2),
(1, 1157788232, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 1249438276, 1958374515, 0, '2025-07-18 17:45', '', '', 20, 1232400, 1),
(1, 1274229703, 1958374515, 0, '2025-07-19 17:38', '', '', 20, 129391, 2),
(1, 1326060234, 1958374515, 0, '2025-07-20 19:45', '', '', 20, 129391, 1),
(1, 1349877020, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 1375423154, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 1431466985, 1958374515, 0, '2025-07-18 21:45', '2025-07-18 22:59', 'fa7385f4451b413b835a9962abe98f1629f12abf05efd990ff644b985b080f5c88782c94fa3739307e60c438deef878a', 20, 1232400, 1),
(1, 1466033492, 1958374515, 0, '2025-07-20 19:45', '', '', 20, 129391, 1),
(1, 1484033645, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 1554196293, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 1572166121, 1958374515, 0, '2025-07-19 19:30', '2025-07-19 19:29', 'fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1', 20, 129391, 2),
(1, 1583138811, 1958374515, 0, '2025-07-19 19:20', '', '', 20, 129391, 2),
(1, 1608343836, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 1686126628, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 1756300212, 1958374515, 0, '2025-07-18 21:45', '2025-07-18 22:34', 'fa7385f4451b413b835a9962abe98f1629f12abf05efd990ff644b985b080f5c88782c94fa3739307e60c438deef878a', 20, 1232400, 1),
(1, 1806567523, 1958374515, 0, '2025-07-19 17:27', '2025-07-19 17:26', 'fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1', 20, 129391, 2),
(1, 1869060445, 1958374515, 0, '2025-07-19 19:45', '', '', 20, 129391, 2),
(1, 1897924026, 1958374515, 0, '2025-07-19 17:42', '', '', 20, 129391, 2),
(1, 1908925245, 1958374515, 0, '2025-07-18 21:45', '', '', 20, 1232400, 1),
(1, 1911500501, 1958374515, 0, '2025-07-19 18:35', '2025-07-19 18:33', 'fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1', 20, 129391, 2),
(1, 1916707354, 1958374515, 0, '2025-07-18 17:45', '', '', 20, 1232400, 1),
(1, 1926147300, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 1967094778, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 1998788411, 1958374515, 0, '2025-07-18 17:45', '', '', 20, 1232400, 1),
(1, 2063837485, 1958374515, 0, '2025-07-20 19:45', '2025-07-20 12:37', 'fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1', 20, 129391, 1),
(1, 2083139029, 1958374515, 0, '2025-07-18 19:45', '', '', 20, 1232400, 1),
(1, 2095064348, 1958374515, 0, '2025-07-20 19:45', '', '', 20, 129391, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `PIATTO`
--

CREATE TABLE `PIATTO` (
  `DISH_ID` int(11) NOT NULL,
  `ID` int(11) NOT NULL,
  `NOME` varchar(50) NOT NULL,
  `DESCRIZIONE` varchar(256) NOT NULL,
  `PREZZO` float NOT NULL,
  `MENU_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `PIATTO`
--

INSERT INTO `PIATTO` (`DISH_ID`, `ID`, `NOME`, `DESCRIZIONE`, `PREZZO`, `MENU_ID`) VALUES
(1, 1, 'carbonara', '\"Jowls, Egg yolks, Pecorino Romano PDO,Black pepper\"', 8, 1),
(2, 23, 'carbonara', 'string', 0, 0),
(3, 24, 'carbonara222', 'aaaaaaaaaaaaaaaaa', 0, 0),
(4, 711305048, 'carbonara', 'carbo', 12, 12),
(5, 1492068332, 'carbo', 'aaaaaaaaaaaaaaaaa', 0, 0),
(6, 1119701121, 'Pane, prosciutto, formaggio', 'Panino con prosciutto', 5, 5),
(7, 1648986338, 'Panino con prosciutto', 'Pane, prosciutto, formaggio', 5, 5),
(8, 1913685968, 'Tagliatelle al ragÃ¹', 'Pasta allâ€™uovo, ragÃ¹', 8, 8),
(9, 11, 'Tagliatelle al ragÃ¹', 'Pasta allâ€™uovo, ragÃ¹', 8, 8),
(10, 3, 'Minestrone', 'Verdure miste, brodo', 5.5, 12321),
(11, 11, 'Tagliatelle al ragÃ¹', 'Pasta allâ€™uovo, ragÃ¹', 8.9, 1212344),
(12, 11, 'Tagliatelle al ragÃ¹', 'Pasta allâ€™uovo, ragÃ¹', 8.9, 33333),
(13, 3, 'Torta salata', 'Pasta sfoglia, verdure', 5.5, 1232400),
(14, 2, 'Melanzane alla parmigiana', 'Melanzane, pomodoro, formaggio', 8, 121244444),
(15, 4, 'Gnocchi al pesto', 'Pesto, patate, gnocchi', 7, 129391);

-- --------------------------------------------------------

--
-- Struttura della tabella `SOCIETÀ_CONSEGNA`
--

CREATE TABLE `SOCIETÀ_CONSEGNA` (
  `ID` int(11) NOT NULL,
  `NOME` varchar(50) NOT NULL,
  `INDIRIZZO` varchar(100) NOT NULL,
  `PREZZO` int(11) NOT NULL,
  `DISPONIBILE` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `SOCIETÀ_CONSEGNA`
--

INSERT INTO `SOCIETÀ_CONSEGNA` (`ID`, `NOME`, `INDIRIZZO`, `PREZZO`, `DISPONIBILE`) VALUES
(2, 'cimangiamo.romanellas.cloud', 'Via Mondo 14, Bologna', 10, 1),
(3, 'famechimica.romanellas.cloud', 'Via edoardo Ferravilla 19,Bologna', 12, 1),
(4, 'panzafly.romanellas.cloud', 'Via delle Lame 116, Bologna', 5, 1),
(5, 'toctocgnam.romanellas.cloud', 'Via Edmondo de Amicis 14, Campobasso ', 12, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `UTENTE`
--

CREATE TABLE `UTENTE` (
  `ID` int(11) NOT NULL,
  `INDIRIZZO` varchar(50) NOT NULL,
  `EMAIL` varchar(50) NOT NULL,
  `PWD` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `UTENTE`
--

INSERT INTO `UTENTE` (`ID`, `INDIRIZZO`, `EMAIL`, `PWD`) VALUES
(1, 'via verdi, Campobasso CB', 'g.zemignani@studenti.unimol.it', 'ahahah'),
(4, 'prova 1', 'sssss', 'aaaaaaaaaaaaa');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `LOCALE`
--
ALTER TABLE `LOCALE`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `MENU`
--
ALTER TABLE `MENU`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `ORDINE`
--
ALTER TABLE `ORDINE`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `PIATTO`
--
ALTER TABLE `PIATTO`
  ADD PRIMARY KEY (`DISH_ID`);

--
-- Indici per le tabelle `SOCIETÀ_CONSEGNA`
--
ALTER TABLE `SOCIETÀ_CONSEGNA`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `UTENTE`
--
ALTER TABLE `UTENTE`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `LOCALE`
--
ALTER TABLE `LOCALE`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1958374516;

--
-- AUTO_INCREMENT per la tabella `MENU`
--
ALTER TABLE `MENU`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1966904465;

--
-- AUTO_INCREMENT per la tabella `ORDINE`
--
ALTER TABLE `ORDINE`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2136033153;

--
-- AUTO_INCREMENT per la tabella `PIATTO`
--
ALTER TABLE `PIATTO`
  MODIFY `DISH_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT per la tabella `SOCIETÀ_CONSEGNA`
--
ALTER TABLE `SOCIETÀ_CONSEGNA`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT per la tabella `UTENTE`
--
ALTER TABLE `UTENTE`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1170620788;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
