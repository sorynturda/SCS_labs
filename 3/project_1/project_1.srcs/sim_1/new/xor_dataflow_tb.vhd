library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity xor_dataflow_tb is
--  Port ( );
end xor_dataflow_tb;

architecture Behavioral of xor_dataflow_tb is

component xor_dataflow is
    port ( a : in STD_LOGIC;
           b : in STD_LOGIC;
           c : out STD_LOGIC);
end component;

constant T : time := 10 ns;
signal a, b, c : std_logic := '0';

begin
    process
        begin
            a <= not a;
            wait for T;
            b <= not b;
            wait for T;
            a <= not a;
        end process;
    xor_map : xor_dataflow port map (
        a => a,
        b => b,
        c => c
    );
end Behavioral;
