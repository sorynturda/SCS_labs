library IEEE;

use IEEE.STD_LOGIC_1164.ALL;
entity decoder_1_8 is
    Port ( a : in STD_LOGIC_VECTOR (2 downto 0);
           b : out STD_LOGIC_VECTOR (7 downto 0));
end decoder_1_8;

architecture Behavioral of decoder_1_8 is

begin

b <= "00000001" when a = "000" else
     "00000010" when a = "001" else
     "00000100" when a = "010" else
     "00001000" when a = "011" else
     "00010000" when a = "100" else
     "00100000" when a = "101" else
     "01000000" when a = "110" else
     "10000000";
end Behavioral;
