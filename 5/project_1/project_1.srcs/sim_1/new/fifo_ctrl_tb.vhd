library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity fifo_ctrl_tb is
end fifo_ctrl_tb;

architecture tb of fifo_ctrl_tb is

component fifo_ctrl is
    Port ( rd : in STD_LOGIC;
           wr : in STD_LOGIC;
           clk : in STD_LOGIC;
           rst : in STD_LOGIC;
           rd_inc : out STD_LOGIC;
           wr_inc : out STD_LOGIC;
           full : out STD_LOGIC;
           empty : out STD_LOGIC);
end component;

signal rd,wr,clk,rst,rd_inc,wr_inc,full,empty : std_logic := '0';
constant T : time := 10ns;

begin
    
    ceas : process
    begin
        clk <= '0';
        wait for T/2;
        clk <= '1';
        wait for T/2;
    end process ceas;
    
    stimuli : process
    begin
        for i in 0 to 7 loop
            wr <= '1';
            wait for T;
            wr <= '0';
            wait for T;
        end loop;
        rd <= '1';
        wait for T;
        rd <= '0';
        wait for T;
    end process stimuli;
    
    fcmap : fifo_ctrl port map(rd,wr,clk,rst,rd_inc,wr_inc,full,empty);
end tb;
