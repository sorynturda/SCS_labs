library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity counter_tb is
end counter_tb;

architecture testbench of counter_tb is

component counter is
    Port ( clk : in STD_LOGIC;
           count : out STD_LOGIC_VECTOR (2 downto 0));
end component;

constant T : time := 10 ns;
signal clk : std_logic := '0';
signal count : std_logic_vector (2 downto 0) := "111";

begin  
    clk <= not clk after T / 2;
    cnt3 : counter port map (
        clk => clk,
        count => count
    );
end testbench;
