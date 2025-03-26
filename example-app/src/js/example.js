import { CompassHeading } from '@yaseenalmufti/compass-heading';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    CompassHeading.echo({ value: inputValue })
}
