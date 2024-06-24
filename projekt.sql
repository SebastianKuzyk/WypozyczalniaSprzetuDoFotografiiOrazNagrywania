-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Cze 24, 2024 at 02:33 PM
-- Wersja serwera: 10.4.32-MariaDB
-- Wersja PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `projekt`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `klienci`
--

CREATE TABLE `klienci` (
  `id` int(11) NOT NULL,
  `name` varchar(60) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `mail` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `Wypozyczenia` varchar(200) DEFAULT NULL,
  `user_type` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `klienci`
--

INSERT INTO `klienci` (`id`, `name`, `surname`, `mail`, `password`, `Wypozyczenia`, `user_type`) VALUES
(2, 'Sebastian', 'Kuzyk', 'sk131461@stud.ur.edu.pl', 'student', '0', NULL),
(3, 'Tomasz', 'Knapik', 'tk123456@stud.ur.edu.pl', 'admin', '0', 'standard'),
(4, 'student', 'stud', 'stud@ur.pl', '123', NULL, NULL);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `sprzet`
--

CREATE TABLE `sprzet` (
  `sprzet_id` int(11) NOT NULL,
  `nazwa` varchar(100) NOT NULL,
  `typ` varchar(50) NOT NULL,
  `producent` varchar(50) NOT NULL,
  `koszt` decimal(10,2) NOT NULL,
  `ilosc_na_stanie` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sprzet`
--

INSERT INTO `sprzet` (`sprzet_id`, `nazwa`, `typ`, `producent`, `koszt`, `ilosc_na_stanie`) VALUES
(6, 'Canon EOS R5 + Adapter EF-EOS R', 'Aparat', 'Canon', 220.00, 5),
(7, 'Canon EOS 6D mark II', 'Aparat', 'Canon', 140.00, 9),
(8, 'Sony A6500', 'Aparat', 'Sony', 100.00, 6),
(9, 'Fujifilm X-T4', 'Aparat', 'Fujifilm', 110.00, 3),
(10, 'Fujifilm INSTAX 300 WIDE', 'Aparat', 'Fujifilm', 40.00, 4),
(11, 'Canon RF 15-35mm F2.8L IS USM', 'Obiektyw', 'Canon', 170.00, 9),
(12, 'Canon Extender RF 2X', 'Obiektyw', 'Canon', 60.00, 12),
(13, 'Canon RF 800mm F11 IS STM', 'Obiektyw', 'Canon', 80.00, 7),
(14, 'Filtr polaryzacyjny Canon PL-C 52WII', 'Obiektyw', 'Canon', 40.00, 5),
(15, 'Sony FE 28 mm f/2.0', 'Obiektyw', 'Sony', 60.00, 6),
(16, 'Kamera GoPro Hero 11 Black', 'Kamera', 'GoPro', 88.00, 2),
(17, 'Kamera Insta360 X3', 'Kamera', 'Insta360', 131.00, 0),
(18, 'Canon SPEEDLITE 430EX II', 'Lampa błyskowa', 'Canon', 50.00, 5),
(19, 'Fujifilm EF-42', 'Lampa błyskowa', 'Fujifilm', 40.00, 2),
(20, 'Canon Speedlite Transmitter ST-E3-RT', 'Lampa błyskowa', 'Canon', 40.00, 5),
(21, 'RodeMicPro', 'Audio', 'Rode', 40.00, 9),
(22, 'Olympus LS-14', 'Audio', 'Olympus', 50.00, 7),
(23, 'Bowens Softbox LUMIAIR OCTABOX 90', 'Sprzet Studyjny', 'Bowens', 40.00, 8),
(24, 'Statyw Fomei LS-13B', 'Sprzet Studyjny', 'Fomei', 30.00, 2),
(25, 'GlareOne Zestaw LED 600 i 1500', 'Sprzet Studyjny', 'GlareOne', 250.00, 3),
(26, 'Blenda Fomei 5w1 120X180cm', 'Sprzet Studyjny', 'Fomei', 30.00, 6),
(27, 'LowePro Runner 200AW', 'Torby', 'LowePro', 10.00, 5),
(28, 'PLECAK Manfrotto PRO 30 MB MP-BP-30BB Czarny', 'Torby', 'Manfrotto', 20.00, 9),
(29, 'Manfrotto statyw 535AQ + Głowica video MVH500AH', 'Statyw', 'Manfrotto', 80.00, 9),
(30, 'Induro Głowica Panoramiczna 3 kierunkowa PHT1', 'Statyw', 'Induro', 20.00, 6),
(31, 'Manfrotto MT055CXPRO3', 'Statyw', 'Manfrotto', 40.00, 12);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `wypozyczenia`
--

CREATE TABLE `wypozyczenia` (
  `ID_wyp` int(11) NOT NULL,
  `sprzet_id` int(11) NOT NULL,
  `ID_klienta` int(11) NOT NULL,
  `Data_wyp` date NOT NULL,
  `Data_zwrotu` date NOT NULL,
  `koszt` decimal(10,2) NOT NULL,
  `ilosc_wypozyczona` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wypozyczenia`
--

INSERT INTO `wypozyczenia` (`ID_wyp`, `sprzet_id`, `ID_klienta`, `Data_wyp`, `Data_zwrotu`, `koszt`, `ilosc_wypozyczona`) VALUES
(9, 18, 2, '2024-06-19', '2024-06-26', 50.00, 2),
(10, 16, 2, '2024-06-19', '2024-06-26', 88.00, 3);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `klienci`
--
ALTER TABLE `klienci`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `sprzet`
--
ALTER TABLE `sprzet`
  ADD PRIMARY KEY (`sprzet_id`);

--
-- Indeksy dla tabeli `wypozyczenia`
--
ALTER TABLE `wypozyczenia`
  ADD PRIMARY KEY (`ID_wyp`),
  ADD KEY `sprzet_id` (`sprzet_id`),
  ADD KEY `ID_klienta` (`ID_klienta`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `klienci`
--
ALTER TABLE `klienci`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `sprzet`
--
ALTER TABLE `sprzet`
  MODIFY `sprzet_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `wypozyczenia`
--
ALTER TABLE `wypozyczenia`
  MODIFY `ID_wyp` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `wypozyczenia`
--
ALTER TABLE `wypozyczenia`
  ADD CONSTRAINT `wypozyczenia_ibfk_1` FOREIGN KEY (`sprzet_id`) REFERENCES `sprzet` (`sprzet_id`),
  ADD CONSTRAINT `wypozyczenia_ibfk_2` FOREIGN KEY (`ID_klienta`) REFERENCES `klienci` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
