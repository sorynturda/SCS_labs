library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity mux_tb is
end mux_tb;

architecture tb of mux_tb is

component mux is
    Port ( a, b, c, d : in STD_LOGIC_VECTOR (3 downto 0);
           sel : in STD_LOGIC_VECTOR (1 downto 0);
           x : out STD_LOGIC_VECTOR (3 downto 0));
end component;

signal a, b, c, d, x : std_logic_vector(3 downto 0) := (others => '0');
signal sel : std_logic_vector(1 downto 0) := (others => '0');
constant T : time := 10ns;

begin
    s : process
    begin
        a <= "1111";
        wait for T;
        b <= "0111";
        wait for T;
        c <= "1010";
        wait for T;
        d <= "0101";
        wait for T;
        sel <= "11";
        wait for T;
        sel <= "00";
        wait for T;
        sel <= "10";
        wait for T;
        sel <= "01";
    end process s;
    
    
    map_mux : mux port map(
        a => a,
        b => b,
        c => c,
        d => d,
        sel => sel,
        x => x
    );
end tb;
