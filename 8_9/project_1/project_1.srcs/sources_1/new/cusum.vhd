library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity cusum is
    Port ( 
        aclk : IN STD_LOGIC;
        aresetn : in STD_LOGIC;
        
        a_tvalid : IN STD_LOGIC;
        a_tready : OUT STD_LOGIC;
        a_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        
        b_tvalid : IN STD_LOGIC;
        b_tready : OUT STD_LOGIC;
        b_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);

        drift_tvalid : IN STD_LOGIC;
        drift_tready : OUT STD_LOGIC;
        drift_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);

        threshold_tvalid : IN STD_LOGIC;
        threshold_tready : OUT STD_LOGIC;
        threshold_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);


        result_tvalid : OUT STD_LOGIC;
        result_tready : IN STD_LOGIC;
        result_tdata : OUT STD_LOGIC
    );  
end cusum;

architecture Behavioral of cusum is

component int_subtractor is
    Port ( 
        aclk : IN STD_LOGIC;
        s_axis_a_tvalid : IN STD_LOGIC;
        s_axis_a_tready : OUT STD_LOGIC;
        s_axis_a_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        s_axis_b_tvalid : IN STD_LOGIC;
        s_axis_b_tready : OUT STD_LOGIC;
        s_axis_b_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        m_axis_result_tvalid : OUT STD_LOGIC;
        m_axis_result_tready : IN STD_LOGIC;
        m_axis_result_tdata : OUT STD_LOGIC_VECTOR(31 DOWNTO 0)
    );
end component;

component int_adder is
    Port ( 
        aclk : IN STD_LOGIC;
        s_axis_a_tvalid : IN STD_LOGIC;
        s_axis_a_tready : OUT STD_LOGIC;
        s_axis_a_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        s_axis_b_tvalid : IN STD_LOGIC;
        s_axis_b_tready : OUT STD_LOGIC;
        s_axis_b_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        m_axis_result_tvalid : OUT STD_LOGIC;
        m_axis_result_tready : IN STD_LOGIC;
        m_axis_result_tdata : OUT STD_LOGIC_VECTOR(31 DOWNTO 0)
    );
end component;

component max is
    Port ( 
        aclk : IN STD_LOGIC;
        s_axis_a_tvalid : IN STD_LOGIC;
        s_axis_a_tready : OUT STD_LOGIC;
        s_axis_a_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        s_axis_b_tvalid : IN STD_LOGIC;
        s_axis_b_tready : OUT STD_LOGIC;
        s_axis_b_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        m_axis_result_tvalid : OUT STD_LOGIC;
        m_axis_result_tready : IN STD_LOGIC;
        m_axis_result_tdata : OUT STD_LOGIC_VECTOR(31 DOWNTO 0)
    );
end component;

component fifo32x64 is
    Port ( 
        s_axis_aresetn : in STD_LOGIC;
        s_axis_aclk : in STD_LOGIC;
        s_axis_tvalid : in STD_LOGIC;
        s_axis_tready : out STD_LOGIC;
        s_axis_tdata : in STD_LOGIC_VECTOR ( 31 downto 0 );
        m_axis_tvalid : out STD_LOGIC;
        m_axis_tready : in STD_LOGIC;
        m_axis_tdata : out STD_LOGIC_VECTOR ( 31 downto 0 )
    );
end component;

component comparator is
    Port ( 
        aclk : IN STD_LOGIC;
        s_axis_a_tvalid : IN STD_LOGIC;
        s_axis_a_tready : OUT STD_LOGIC;
        s_axis_a_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        
        s_axis_b_tvalid : IN STD_LOGIC;
        s_axis_b_tready : OUT STD_LOGIC;
        s_axis_b_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        
        s_axis_threshold_tvalid : IN STD_LOGIC;
        s_axis_threshold_tready : OUT STD_LOGIC;
        s_axis_threshold_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        
        m_axis_g_plus_tvalid : OUT STD_LOGIC;
        m_axis_g_plus_tready : IN STD_LOGIC;
        m_axis_g_plus_tdata : OUT STD_LOGIC_VECTOR(31 DOWNTO 0);
        
        m_axis_label_tvalid : OUT STD_LOGIC;
        m_axis_label_tready : IN STD_LOGIC;
        m_axis_label_tdata : OUT STD_LOGIC;
        
        m_axis_g_minus_tvalid : OUT STD_LOGIC;
        m_axis_g_minus_tready : IN STD_LOGIC;
        m_axis_g_minus_tdata : OUT STD_LOGIC_VECTOR(31 DOWNTO 0)
    );
end component;



signal temp1_tdata, temp2_tdata, temp3_tdata, temp4_tdata, temp5_tdata, dif_tdata1, dif_tdata2, dif_tdata3, dif_tdata4 : STD_LOGIC_VECTOR (31 downto 0);
signal temp1_tready, temp2_tready, temp3_tready, temp4_tready, temp5_tready, dif_tready1, dif_tready2, dif_tready3, dif_tready4 : STD_LOGIC;
signal temp1_tvalid, temp2_tvalid, temp3_tvalid, temp4_tvalid, temp5_tvalid, dif_tvalid1, dif_tvalid2, dif_tvalid3, dif_tvalid4 : STD_LOGIC;

signal g_plus_tdata, g_minus_tdata, sum_tdata, temp6_tdata, temp7_tdata, temp8_tdata, temp9_tdata : STD_LOGIC_VECTOR (31 downto 0);
signal g_plus_tready, g_minus_tready, sum_tready, temp6_tready, temp7_tready, temp8_tready, temp9_tready : STD_LOGIC;
signal g_plus_tvalid, g_minus_tvalid, sum_tvalid, temp6_tvalid, temp7_tvalid, temp8_tvalid, temp9_tvalid  : STD_LOGIC;

signal max1_tdata, max2_tdata, temp10_tdata, temp11_tdata : STD_LOGIC_VECTOR (31 downto 0);
signal max1_tready, max2_tready, temp10_tready, temp11_tready : STD_LOGIC;
signal max1_tvalid, max2_tvalid, temp10_tvalid, temp11_tvalid  : STD_LOGIC;

begin
    -- fifo se numara de sus in jos si de la stanga la dreapta
    fifo_in_xt : fifo32x64 port map ( -- 1
        s_axis_aresetn => aresetn,
        s_axis_aclk => aclk,
        s_axis_tvalid => a_tvalid,
        s_axis_tready => a_tready,
        s_axis_tdata => a_tdata,
        m_axis_tvalid => temp1_tvalid,
        m_axis_tready => temp1_tready,
        m_axis_tdata => temp1_tdata
    );

    fifo_in_xt_minus_1 : fifo32x64 port map ( -- 2
        s_axis_aresetn => aresetn,
        s_axis_aclk => aclk,
        s_axis_tvalid => b_tvalid,
        s_axis_tready => b_tready,
        s_axis_tdata => b_tdata,
        m_axis_tvalid => temp2_tvalid,
        m_axis_tready => temp2_tready,
        m_axis_tdata => temp2_tdata
    );
    
    dif_1: int_subtractor port map(  
        aclk => aclk,
        s_axis_a_tvalid => temp1_tvalid,
        s_axis_a_tready => temp1_tready, 
        s_axis_a_tdata => temp1_tdata,
        s_axis_b_tvalid => temp2_tvalid,
        s_axis_b_tready => temp2_tready,
        s_axis_b_tdata => temp2_tdata,
        m_axis_result_tvalid => dif_tvalid1,
        m_axis_result_tready => dif_tready1,
        m_axis_result_tdata => dif_tdata1
    ); 
        
    fifo_in_dif1 : fifo32x64 port map ( -- 3
        s_axis_aresetn => aresetn,
        s_axis_aclk => aclk,
        s_axis_tvalid => dif_tvalid1,
        s_axis_tready => dif_tready1,
        s_axis_tdata => dif_tdata1,
        m_axis_tvalid => temp3_tvalid,
        m_axis_tready => temp3_tready,
        m_axis_tdata => temp3_tdata
    );

    fifo_in_g_plus : fifo32x64 port map ( -- 4
        s_axis_aresetn => aresetn,
        s_axis_aclk => aclk,
        s_axis_tvalid => g_plus_tvalid,
        s_axis_tready => g_plus_tready,
        s_axis_tdata => g_plus_tdata,
        m_axis_tvalid => temp4_tvalid,
        m_axis_tready => temp4_tready,
        m_axis_tdata => temp4_tdata
    );       
       
    fifo_in_g_minus : fifo32x64 port map ( -- 5
        s_axis_aresetn => aresetn,
        s_axis_aclk => aclk,
        s_axis_tvalid => g_minus_tvalid,
        s_axis_tready => g_minus_tready,
        s_axis_tdata => g_minus_tdata,
        m_axis_tvalid => temp5_tvalid,
        m_axis_tready => temp5_tready,
        m_axis_tdata => temp5_tdata
    );  
        
    add_1: int_adder port map( 
        aclk => aclk,
        s_axis_a_tvalid => temp3_tvalid,
        s_axis_a_tready => temp3_tready, 
        s_axis_a_tdata => temp3_tdata,
        s_axis_b_tvalid => temp4_tvalid,
        s_axis_b_tready => temp4_tready,
        s_axis_b_tdata => temp4_tdata,
        m_axis_result_tvalid => sum_tvalid,
        m_axis_result_tready => sum_tready,
        m_axis_result_tdata => sum_tdata
    );         
    
    fifo_in_add : fifo32x64 port map ( -- 6
        s_axis_aresetn => aresetn,
        s_axis_aclk => aclk,
        s_axis_tvalid => sum_tvalid,
        s_axis_tready => sum_tready,
        s_axis_tdata => sum_tdata,
        m_axis_tvalid => temp6_tvalid,
        m_axis_tready => temp6_tready,
        m_axis_tdata => temp6_tdata
    );

    dif_2: int_subtractor port map(  
        aclk => aclk,
        s_axis_a_tvalid => temp3_tvalid,
        s_axis_a_tready => temp3_tready, 
        s_axis_a_tdata => temp3_tdata,
        s_axis_b_tvalid => temp5_tvalid,
        s_axis_b_tready => temp5_tready,
        s_axis_b_tdata => temp5_tdata,
        m_axis_result_tvalid => dif_tvalid2,
        m_axis_result_tready => dif_tready2,
        m_axis_result_tdata => dif_tdata2
    ); 

    fifo_in_dif2 : fifo32x64 port map ( -- 7
        s_axis_aresetn => aresetn,
        s_axis_aclk => aclk,
        s_axis_tvalid => dif_tvalid2,
        s_axis_tready => dif_tready2,
        s_axis_tdata => dif_tdata2,
        m_axis_tvalid => temp7_tvalid,
        m_axis_tready => temp7_tready,
        m_axis_tdata => temp7_tdata
    );

    dif_3: int_subtractor port map(  -- se scade drift
        aclk => aclk,
        s_axis_a_tvalid => temp6_tvalid,
        s_axis_a_tready => temp6_tready, 
        s_axis_a_tdata => temp6_tdata,
        s_axis_b_tvalid => drift_tvalid,
        s_axis_b_tready => drift_tready,
        s_axis_b_tdata => drift_tdata,
        m_axis_result_tvalid => dif_tvalid3,
        m_axis_result_tready => dif_tready3,
        m_axis_result_tdata => dif_tdata3
    ); 

    fifo_in_dif3 : fifo32x64 port map ( -- 8
        s_axis_aresetn => aresetn,
        s_axis_aclk => aclk,
        s_axis_tvalid => dif_tvalid3,
        s_axis_tready => dif_tready3,
        s_axis_tdata => dif_tdata3,
        m_axis_tvalid => temp8_tvalid,
        m_axis_tready => temp8_tready,
        m_axis_tdata => temp8_tdata
    );
        
    dif_4: int_subtractor port map(  -- se scade drift
        aclk => aclk,
        s_axis_a_tvalid => temp7_tvalid,
        s_axis_a_tready => temp7_tready, 
        s_axis_a_tdata => temp7_tdata,
        s_axis_b_tvalid => drift_tvalid,
        s_axis_b_tready => drift_tready,
        s_axis_b_tdata => drift_tdata,
        m_axis_result_tvalid => dif_tvalid4,
        m_axis_result_tready => dif_tready4,
        m_axis_result_tdata => dif_tdata4
    ); 
    
    fifo_in_dif4 : fifo32x64 port map ( -- 9
        s_axis_aresetn => aresetn,
        s_axis_aclk => aclk,
        s_axis_tvalid => dif_tvalid4,
        s_axis_tready => dif_tready4,
        s_axis_tdata => dif_tdata4,
        m_axis_tvalid => temp9_tvalid,
        m_axis_tready => temp9_tready,
        m_axis_tdata => temp9_tdata
    );
        
     max_1 : max port map (
        aclk => aclk,
        s_axis_a_tvalid => temp8_tvalid,
        s_axis_a_tready => temp8_tready,
        s_axis_a_tdata => temp8_tdata,
        s_axis_b_tvalid => '1',
        s_axis_b_tready => open,
        s_axis_b_tdata => x"00000000",
        m_axis_result_tvalid => max1_tvalid,
        m_axis_result_tready => '1',
        m_axis_result_tdata => max1_tdata
     );   

    fifo_in_max1 : fifo32x64 port map ( -- 10
        s_axis_aresetn => aresetn,
        s_axis_aclk => aclk,
        s_axis_tvalid => max1_tvalid,
        s_axis_tready => max1_tready,
        s_axis_tdata => max1_tdata,
        m_axis_tvalid => temp10_tvalid,
        m_axis_tready => temp10_tready,
        m_axis_tdata => temp10_tdata
    );

    max_2 : max port map (
        aclk => aclk,
        s_axis_a_tvalid => temp9_tvalid,
        s_axis_a_tready => temp9_tready,
        s_axis_a_tdata => temp9_tdata,
        s_axis_b_tvalid => '1',
        s_axis_b_tready => open,
        s_axis_b_tdata => x"00000000",
        m_axis_result_tvalid => max2_tvalid,
        m_axis_result_tready => '1',
        m_axis_result_tdata => max2_tdata
    );
    
    
    fifo_in_max2 : fifo32x64 port map ( -- 11
        s_axis_aresetn => aresetn,
        s_axis_aclk => aclk,
        s_axis_tvalid => max2_tvalid,
        s_axis_tready => max2_tready,
        s_axis_tdata => max2_tdata,
        m_axis_tvalid => temp11_tvalid,
        m_axis_tready => temp11_tready,
        m_axis_tdata => temp11_tdata
    );

    th_exc_comp : comparator port map(
        aclk => aclk,
        s_axis_a_tvalid => temp10_tvalid, 
        s_axis_a_tready => temp10_tready, 
        s_axis_a_tdata => temp10_tdata,
        s_axis_b_tvalid => temp11_tvalid, 
        s_axis_b_tready => temp11_tready, 
        s_axis_b_tdata => temp11_tdata,
        s_axis_threshold_tvalid => threshold_tvalid, 
        s_axis_threshold_tready => threshold_tready, 
        s_axis_threshold_tdata => threshold_tdata,
        m_axis_g_plus_tvalid => g_plus_tvalid,
        m_axis_g_plus_tready => g_plus_tready,
        m_axis_g_plus_tdata => g_plus_tdata,
        m_axis_label_tvalid => result_tvalid, 
        m_axis_label_tready => result_tready,
        m_axis_label_tdata => result_tdata,
        m_axis_g_minus_tvalid => g_minus_tvalid,
        m_axis_g_minus_tready => g_minus_tready, 
        m_axis_g_minus_tdata => g_minus_tdata
    );

end Behavioral;
