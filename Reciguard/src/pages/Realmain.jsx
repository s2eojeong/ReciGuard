import React from 'react';
import Header2 from '../components/Header2';
import Footer from '../components/Footer';
import Recommend from '../components/Recommend';
import Morerec from '../components/Morerec';
import Myrec from '../components/Myrec';

function Realmain() {
    return (
        <div>
            <Header2/>
            <Recommend />
            <Morerec />
            <Myrec />
            <Footer/>
        </div>
    )
}

export default Realmain;