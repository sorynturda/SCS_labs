library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity decoder_1_8_tb is
end decoder_1_8_tb;

architecture tb of decoder_1_8_tb is

component decoder_1_8 is
    Port ( a : in STD_LOGIC_VECTOR (2 downto 0);
           b : out STD_LOGIC_VECTOR (7 downto 0));
end component ;

signal a : std_logic_vector (2 downto 0) := (others => '0');
signal b : std_logic_vector (7 downto 0) := (others => '0');
constant T : time := 10 ns;

begin
    
    process
    begin
        a <= "101";
        wait for T;
        a <= "111";
        wait for T;
        a <= "110";
        wait for T;
        a <= "010";
        wait for T;
        a <= "001";
        wait for T;
    end process;
    
    mapare : decoder_1_8 port map(
        a => a,
        b => b
    ); 
end tb;
