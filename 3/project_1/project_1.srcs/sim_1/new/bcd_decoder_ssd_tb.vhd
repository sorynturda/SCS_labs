library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity bcd_decoder_ssd_tb is
end bcd_decoder_ssd_tb;

architecture tb of bcd_decoder_ssd_tb is

component bcd_decoder_ssd is
    Port ( a : in STD_LOGIC_VECTOR (3 downto 0);
           b : out STD_LOGIC_VECTOR (6 downto 0));
end component;

signal a : STD_LOGIC_VECTOR (3 downto 0) := (others => '0');
signal b : STD_LOGIC_VECTOR (6 downto 0) := (others => '0');
constant T : time := 10ns;

begin
    s : process
    begin
        wait for T;
        a <= "0001";
        wait for T;
        a <= "0010";
        wait for T;
        a <= "0011";
        wait for T;
        a <= "0100";
        wait for T;
        a <= "0101";
        wait for T;
        a <= "0110";
        wait for T;
        a <= "0111";
        wait for T;
        a <= "1000";
        wait for T;
        a <= "1001";
        wait for T;
        a <= "1010";
        wait for T;
        a <= "1011";
        wait for T;
        a <= "1100";
        wait for T;
        a <= "1101";
        wait for T;
        a <= "1110";
        wait for T;
        a <= "1111";
        wait for T;
    end process s;    

    mapare : bcd_decoder_ssd port map(
        a => a,
        b => b
    ); 
end tb;
